# Kestros Validation API

Interfaces, abstract classes, and common validators for the Kestros model validation framework.

## Purpose

`kestros-validation-api` defines the contract for model validation in the Kestros platform. It provides the `ModelValidator`, `ModelValidatorBundle`, `ModelValidationResult`, and `ValidatorResult` abstractions, along with service interfaces for registering validators against model types and running validation.

This is the API layer of the validation framework. The implementation lives in `kestros-validation-core`. Any module that defines custom validators or consumes validation results depends on this bundle.

## Installation & Build

**Maven coordinates:**

```
io.kestros.commons:kestros-validation-api
```

**Build:**

```bash
mvn clean package
```

**Deploy to a Sling instance:**

```bash
mvn clean install -P installBundle -Dsling.host=localhost -Dsling.port=8080
```

## Configuration

No OSGi configuration required. This bundle provides interfaces and abstract classes only.

## API / Service Usage

### Model Validation Message Types

```java
public enum ModelValidationMessageType {
    ERROR,    // Validation failure that prevents use
    WARNING,  // Non-blocking issue
    INFO      // Informational message
}
```

### Validator Classes

#### `ModelValidator<T extends BaseSlingModel>`

Abstract base class for a single validation rule. Each validator checks one condition on a model.

| Method | Returns | Description |
|--------|---------|-------------|
| `isValidCheck(T model)` | `Boolean` | Whether the model passes this validation rule |
| `getMessage()` | `String` | Message displayed when validation fails |
| `getDetailedMessage(T model)` | `String` | Detailed failure message with model context |
| `getType()` | `ModelValidationMessageType` | Severity level (ERROR, WARNING, or INFO) |

**Creating a custom validator:**

```java
public static <T extends BaseResource> ModelValidator<T> hasTitle() {
    return new ModelValidator<T>() {
        @Override
        public Boolean isValidCheck(T model) {
            return StringUtils.isNotEmpty(((BaseResource) model).getTitle());
        }

        @Override
        public String getMessage() {
            return "Title is configured.";
        }

        @Override
        public String getDetailedMessage(T model) {
            return "The jcr:title property must be configured.";
        }

        @Override
        public ModelValidationMessageType getType() {
            return ModelValidationMessageType.ERROR;
        }
    };
}
```

#### `DocumentedModelValidator<T extends BaseSlingModel>`

Extended `ModelValidator` that includes a documentation resource type, allowing the UI to link validation messages to documentation pages.

#### `ModelValidatorBundle<T extends BaseSlingModel>`

A composite validator that groups multiple `ModelValidator` instances. Can require all validators to pass (`isAllMustBeTrue() = true`) or just one (`isAllMustBeTrue() = false`).

| Method | Returns | Description |
|--------|---------|-------------|
| `registerValidators()` | `void` | Override to add validators via `addValidator()` |
| `isAllMustBeTrue()` | `boolean` | Whether all contained validators must pass |
| `addValidator(validator)` | `void` | Adds a single validator to the bundle |
| `addAllValidators(list)` | `void` | Adds a list of validators to the bundle |
| `getValidators()` | `List<ModelValidator<T>>` | All validators in this bundle |
| `getType()` | `ModelValidationMessageType` | Highest severity level among contained validators |

### Result Interfaces

#### `ModelValidationResult`

Represents the complete validation result for a model, aggregating results from all registered validators.

| Method | Returns | Description |
|--------|---------|-------------|
| `getResults()` | `List<ValidatorResult>` | All individual validator results |
| `getModel()` | `<T extends BaseSlingModel>` | The model that was validated |
| `getValidators()` | `List<ModelValidator>` | The validators that were executed |
| `isValid()` | `boolean` | Whether all validators passed |
| `getMessages()` | `Map<ModelValidationMessageType, List<String>>` | Messages grouped by severity |

#### `ValidatorResult`

Represents the result of a single validator execution.

| Method | Returns | Description |
|--------|---------|-------------|
| `isValid()` | `boolean` | Whether this validator passed |
| `getMessage()` | `String` | Validation message |
| `getDetailedMessage()` | `String` | Detailed validation message |
| `getDocumentationResourceType()` | `String` | Resource type for documentation linkage |
| `getBundled()` | `List<ValidatorResult>` | Results from bundled sub-validators |
| `getValidatorClassPath()` | `String` | Class path of the validator that was executed |
| `getType()` | `ModelValidationMessageType` | Severity level |
| `getMessages()` | `Map<ModelValidationMessageType, List<String>>` | Messages grouped by severity |

### Service Interfaces

#### `ModelValidationService`

Service interface for validating models. Extends `ManagedService`.

```java
public interface ModelValidationService extends ManagedService {
    <T extends BaseResource> ModelValidationResult validate(T model);
}
```

#### `ModelValidatorRegistrationService`

Interface for services that provide validators for a specific model type.

| Method | Returns | Description |
|--------|---------|-------------|
| `getModelType()` | `Class<? extends BaseSlingModel>` | The model type these validators apply to |
| `getModelValidators()` | `List<ModelValidator>` | The validators to register |
| `getModelValidatorRegistrationHandlerService()` | `ModelValidatorRegistrationHandlerService` | The handler service for registration |

#### `BaseModelValidationRegistrationService`

Abstract base class that auto-registers validators on OSGi activation and unregisters on deactivation.

```java
@Component(service = ModelValidatorRegistrationService.class, immediate = true)
public class MyModelValidationRegistrationService
        extends BaseModelValidationRegistrationService {

    @Reference
    private ModelValidatorRegistrationHandlerService handler;

    @Override
    public Class<? extends BaseSlingModel> getModelType() {
        return MyResource.class;
    }

    @Override
    public List<ModelValidator> getModelValidators() {
        return List.of(CommonValidators.hasTitle(), myCustomValidator());
    }

    @Override
    public ModelValidatorRegistrationHandlerService getModelValidatorRegistrationHandlerService() {
        return handler;
    }
}
```

#### `ModelValidatorRegistrationHandlerService`

Central handler that manages the global registry of validators mapped to model types.

| Method | Description |
|--------|-------------|
| `getRegisteredModelValidatorMap()` | Returns the full map of model types to their registered validators |
| `registerAllValidatorsFromAllServices()` | Registers validators from all known registration services |
| `registerAllValidatorsFromService(service)` | Registers validators from a specific service |
| `unregisterAllValidatorsFromService(service)` | Unregisters validators from a specific service |
| `registerValidators(validators, type)` | Adds validators for a model type |
| `removeValidators(validators, type)` | Removes validators for a model type |

#### `ModelValidationCacheService`

Cache service interface for validation results (extends `CacheService`).

#### `ModelValidatorProviderService`

Service interface for providing validators for a given model (used internally by the validation framework).

### CommonValidators

Utility class with pre-built validators for common checks.

| Validator | Description |
|-----------|-------------|
| `hasTitle()` | Checks that the resource has a non-empty `jcr:title` that differs from its node name |
| Additional validators | Check the source for the full list of available common validators |

## Dependencies

### Upstream

| Dependency | Maven Coordinates |
|------------|-------------------|
| kestros-structured-sling-models | `io.kestros.commons:kestros-structured-sling-models` |
| kestros-osgi-service-utils | `io.kestros.commons:kestros-osgi-service-utils` |

### Downstream

| Module | Relationship |
|--------|-------------|
| kestros-validation-core | Implements the interfaces defined here |
| Any module defining custom validators | Depends on the validator and registration abstractions |
| kestros-site-management-core | Uses validation on site/page models |
| kestros-component-types-core | Uses validation on component type models |
