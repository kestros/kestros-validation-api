/*
 * Licensed to the Apache Software Foundation (ASF) under one

 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.kestros.commons.validation.api.utils;


import static io.kestros.commons.validation.api.ModelValidationMessageType.ERROR;
import static io.kestros.commons.validation.api.ModelValidationMessageType.WARNING;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.kestros.commons.structuredslingmodels.BaseResource;
import io.kestros.commons.structuredslingmodels.BaseSlingModel;
import io.kestros.commons.structuredslingmodels.exceptions.ChildResourceNotFoundException;
import io.kestros.commons.structuredslingmodels.exceptions.InvalidResourceTypeException;
import io.kestros.commons.structuredslingmodels.utils.SlingModelUtils;
import io.kestros.commons.validation.api.ModelValidationMessageType;
import io.kestros.commons.validation.api.models.ModelValidationResult;
import io.kestros.commons.validation.api.models.ModelValidator;
import io.kestros.commons.validation.api.models.ModelValidatorBundle;
import io.kestros.commons.validation.api.services.ModelValidationService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility Class which holds static validators which are commonly used.
 */
@SuppressFBWarnings({"PARAMETER_NULLABILITY", "UMTP_UNBOUND_METHOD_TEMPLATE_PARAMETER"})
public class CommonValidators {

  /**
   * Validator that checks if the current Resource has a title value.
   *
   * @param <T> Generic model type.
   *
   * @return Validator that checks if the current Resource has a title value.
   */
  @Nonnull
  public static <T extends BaseResource> ModelValidator hasTitle() {
    return new ModelValidator<T>() {


      @Nonnull
      @Override
      public Boolean isValidCheck(@Nonnull T model) {
        BaseResource resource = model;
        return !resource.getName().equals(resource.getTitle()) && StringUtils.isNotEmpty(
                resource.getTitle());
      }

      @Nonnull
      @Override
      public String getMessage() {
        return "Title is configured.";
      }

      @Nonnull
      @Override
      public String getDetailedMessage(@Nonnull T model) {
        return "The jcr:title property must be configured.";
      }

      @Nonnull
      @Override
      public ModelValidationMessageType getType() {
        return ERROR;
      }
    };
  }

  /**
   * Validator that checks if the current Resource has a description value.
   *
   * @param messageType Message type to return failed validation as.
   * @param <T> Generic model type.
   *
   * @return Validator that checks if the current Resource has a description value.
   */

  @Nonnull
  public static <T extends BaseResource> ModelValidator hasDescription(
          final ModelValidationMessageType messageType) {
    return new ModelValidator<T>() {

      @Nonnull
      @Override
      public Boolean isValidCheck(@Nonnull T model) {
        BaseResource resource = model;
        return StringUtils.isNotEmpty(resource.getDescription());
      }

      @Nonnull
      @Override
      public String getMessage() {
        return "Description is configured.";
      }

      @Nonnull
      @Override
      public String getDetailedMessage(@Nonnull T model) {
        return "The jcr:description property must be configured.";
      }

      @Nonnull
      @Override
      public ModelValidationMessageType getType() {
        return messageType;
      }
    };
  }

  /**
   * Validator that checks if the Resource's name ends with the file extension String.
   *
   * @param extension String to match the Resource's name against.
   * @param messageType Message type to return failed validation as.
   * @param <T> Generic model type.
   *
   * @return Validator that checks if the Resource's name ends with the specified String.
   */
  @Nonnull
  public static <T extends BaseResource> ModelValidator hasFileExtension(
          @Nonnull final String extension, @Nonnull final ModelValidationMessageType messageType) {
    return new ModelValidator<T>() {

      @Nonnull
      @Override
      public Boolean isValidCheck(@Nonnull T model) {
        BaseResource resource = model;
        return resource.getName().endsWith(extension);
      }

      @Nonnull
      @Override
      public String getMessage() {
        return "Resource name ends with " + extension + " extension.";
      }

      @Nonnull
      @Override
      public String getDetailedMessage(@Nonnull T model) {
        if (model != null) {
          return String.format("Filename %s is expected to end with .%s.",
                  model.getResource().getName(), extension);
        } else {
          return String.format("Filename is expected to end with .%s.", extension);
        }
      }

      @Nonnull
      @Override
      public ModelValidationMessageType getType() {
        return messageType;
      }
    };
  }

  /**
   * Validator that checks if a specified child Resource exists.
   *
   * @param childName name of the child Resource to check for.
   * @param <T> Generic model type.
   * @param type Level of validation message to return.
   *
   * @return Validator that checks if a specified child Resource exists.
   */
  @Nonnull
  public static <T extends BaseResource> ModelValidator hasChildResource(
          @Nonnull final String childName, @Nonnull ModelValidationMessageType type) {
    return new ModelValidator<T>() {

      @Nonnull
      @Override
      public Boolean isValidCheck(@Nonnull T model) {
        try {
          SlingModelUtils.getChildAsBaseResource(childName, model.getResource());
        } catch (final ChildResourceNotFoundException exception) {
          return Boolean.FALSE;
        }
        return Boolean.TRUE;
      }

      @Nonnull
      @Override
      public String getMessage() {
        return String.format("Has child resource '%s'.", childName);
      }

      @Nonnull
      @Override
      public String getDetailedMessage(@Nonnull T model) {
        return String.format("Expected child resource '%s' was not found.", childName);
      }

      @Nonnull
      @Override
      public ModelValidationMessageType getType() {
        return type;
      }
    };
  }

  /**
   * Validator that checks if a specified child does not fail any ERROR type validators.
   *
   * @param childName name of the child Resource to check for.
   * @param childType type of Model to attempt to adapt the child to. Must extend
   *         BaseResource.
   * @param <T> Generic model type.
   * @param <S> Generic model type.
   * @param type Level of validation message to return.
   *
   * @return Validator that checks if a specified child does not fail any ERROR type validators.
   */
  @Nonnull
  public static <T extends BaseResource, S extends BaseResource> ModelValidator
      isChildResourceValidResourceType(@Nonnull final String childName,
          @Nonnull final Class<S> childType, @Nonnull ModelValidationMessageType type) {

    return new ModelValidator<T>() {

      @Nonnull
      @Override
      public Boolean isValidCheck(@Nonnull BaseResource model) {
        try {
          SlingModelUtils.getChildAsType(childName, model.getResource(), childType);
        } catch (InvalidResourceTypeException e) {
          return Boolean.FALSE;
        } catch (ChildResourceNotFoundException e) {
          return Boolean.TRUE;
        }
        return Boolean.TRUE;
      }

      @Nonnull
      @Override
      public String getMessage() {
        return String.format("Has valid child resource '%s'.", childName);
      }

      @Nonnull
      @Override
      public String getDetailedMessage(@Nonnull T model) {
        return String.format(
                "Child resource '%s' could not be adapted to %s. Likely the wrong resourceType.",
                childName, childType.getSimpleName());
      }

      @Nonnull
      @Override
      public ModelValidationMessageType getType() {
        return type;
      }
    };
  }

  /**
   * Model Validator Bundle that checks if a specified child resource exists, and is valid.
   *
   * @param childName name of the child Resource to check for.
   * @param childType type of Model to attempt to adapt the child to. Must extend
   *         BaseResource.
   * @param <T> Generic model type.
   * @param <S> Generic model type.
   * @param messageType Message type to return failed validation as.
   *
   * @return Model Validator Bundle that checks if a specified child resource exists, and is valid.
   */
  @Nonnull
  public static <T extends BaseResource, S extends BaseResource> ModelValidatorBundle<T>
      hasValidChild(@Nonnull final String childName, @Nonnull final Class<S> childType,
          @Nonnull final ModelValidationMessageType messageType) {
    return new ModelValidatorBundle<T>() {

      @Nonnull
      @Override
      public String getMessage() {
        return "Has valid child " + childType.getSimpleName() + " '" + childName + "'";
      }


      @Nonnull
      @Override
      public void registerValidators() {
        addValidator(hasChildResource(childName, messageType));
        addValidator(isChildResourceValidResourceType(childName, childType, messageType));
      }

      @Nonnull
      @Override
      public ModelValidationMessageType getType() {
        return messageType;
      }

      @Nonnull
      @Override
      public boolean isAllMustBeTrue() {
        return true;
      }
    };
  }

  /**
   * Builds a List of failed Error validators from a model.
   *
   * @param model model to build Validator List from.
   * @param <T> Generic type
   * @param modelValidationResult ModelValidationResult to build validators from.
   *
   * @return List of failed Error validators from a model.
   */
  @Nonnull
  public static <T extends BaseResource> List<ModelValidator> getFailedErrorValidators(
          @Nonnull final T model, @Nonnull ModelValidationResult modelValidationResult) {
    List<String> errorMessages = modelValidationResult.getMessages().get(ERROR);
    final List<ModelValidator> errorValidators = new ArrayList<>(errorMessages.size());
    for (final String errorMessage : errorMessages) {
      final ModelValidator validator = new ModelValidator<T>() {


        @Nonnull
        @Override
        public Boolean isValidCheck(@Nonnull T model) {
          return Boolean.FALSE;
        }

        @Nonnull
        @Override
        public String getMessage() {
          return "Error validator failed for " + model.getPath() + ": " + errorMessage;
        }

        @Nonnull
        @Override
        public String getDetailedMessage(@Nonnull T model) {
          return StringUtils.EMPTY;
        }

        @Nonnull
        @Override
        public ModelValidationMessageType getType() {
          return ERROR;
        }
      };
      errorValidators.add(validator);
    }
    return errorValidators;
  }

  /**
   * Builds a List of failed Warning validators from a model.
   *
   * @param model model to build Validator List from.
   * @param <T> Generic type
   * @param modelValidationResult ModelValidationResult to build validators from.
   *
   * @return List of failed Error Warning from a model.
   */
  @Nonnull
  public static <T extends BaseResource> List<ModelValidator> getFailedWarningValidators(
          @Nonnull final T model, @Nonnull ModelValidationResult modelValidationResult) {
    List<String> warningMessages = modelValidationResult.getMessages().get(WARNING);
    final List<ModelValidator> warningValidators = new ArrayList<>(warningMessages.size());
    for (final String warningMessage : warningMessages) {
      final ModelValidator validator = new ModelValidator<T>() {

        @Nonnull
        @Override
        public Boolean isValidCheck(@Nonnull T model) {
          return Boolean.FALSE;
        }

        @Nonnull
        @Override
        public String getMessage() {
          return "Warning validator failed for " + model.getPath() + ": " + warningMessage;
        }

        @Nonnull
        @Override
        public String getDetailedMessage(@Nonnull T model) {
          return "";
        }

        @Nonnull
        @Override
        public ModelValidationMessageType getType() {
          return WARNING;
        }
      };
      warningValidators.add(validator);
    }
    return warningValidators;
  }

  /**
   * Checks a list for null values.
   *
   * @param list List to check.
   * @param message Validation message.
   * @param type Validation error level.
   * @param <T> Generic type.
   *
   * @return ModelValidator for null values in a specified list.
   */
  @Nonnull
  public static <T> ModelValidator listContainsNoNulls(@Nonnull final List<T> list,
          @Nonnull final String message, @Nonnull final ModelValidationMessageType type) {

    return new ModelValidator() {

      @Nonnull
      @Override
      public Boolean isValidCheck(@Nonnull BaseSlingModel model) {
        for (final Object object : list) {
          if (object == null) {
            return Boolean.FALSE;
          }
        }
        return Boolean.TRUE;
      }

      @Nonnull
      @Override
      public String getMessage() {
        return message;
      }

      @Nonnull
      @Override
      public String getDetailedMessage(@Nonnull BaseSlingModel model) {
        return getMessage();
      }

      @Nonnull
      @Override
      public ModelValidationMessageType getType() {
        return type;
      }
    };
  }

  /**
   * Validates whether a specified list of models has any error messages.
   *
   * @param modelList models to check for errors.
   * @param message Validation message
   * @param <T> Extends base Resource.
   * @param detailedMessage Detailed message to return if validation fails.
   * @param modelValidationService ModelValidationService to use for validation.
   *
   * @return Validates whether a specified list of models has any error messages.
   */
  @Nonnull
  public static <T extends BaseResource> ModelValidator modelListHasNoErrors(
          @Nonnull final List<T> modelList, @Nonnull final String message,
          @Nonnull final String detailedMessage,
          @Nonnull ModelValidationService modelValidationService) {
    return modelListHasNoFailedValidatorsOfType(modelList, message, detailedMessage, ERROR,
            modelValidationService);
  }

  /**
   * Validates whether a specified list of models has any warning messages.
   *
   * @param modelList models to check for warnings.
   * @param message Validation message
   * @param <T> Extends base Resource.
   * @param detailedMessage Detailed message to return if validation fails.
   * @param modelValidationService ModelValidationService to use for validation.
   *
   * @return Validates whether a specified list of models has any warning messages.
   */
  @Nonnull
  public static <T extends BaseResource> ModelValidator modelListHasNoWarnings(
          @Nonnull final List<T> modelList, @Nonnull final String message,
          @Nonnull final String detailedMessage,
          @Nonnull final ModelValidationService modelValidationService) {
    return modelListHasNoFailedValidatorsOfType(modelList, message, detailedMessage, WARNING,
            modelValidationService);
  }

  @Nonnull
  static <T extends BaseResource> ModelValidator modelListHasNoFailedValidatorsOfType(
          @Nonnull List<T> modelList, String message, @Nonnull String detailedMessage,
          @Nonnull ModelValidationMessageType type,
          @Nonnull ModelValidationService validationService) {
    return new ModelValidator<T>() {

      @Nonnull
      @Override
      public Boolean isValidCheck(@Nonnull T model) {
        for (@Nonnull T model1 : modelList) {
          ModelValidationResult result = validationService.validate(model1);
          if (ERROR.name().equals(type.name())) {
            if (result.getMessages().get(ERROR) != null
                    && result.getMessages().get(ERROR).size() > 0) {
              return Boolean.FALSE;
            }
            //  if (result.getMessages().get(WARNING) != null
            //        &&result.getMessages().get(WARNING).size() > 0){
            //  return Boolean.FALSE;
            // }
          } else if (WARNING.name().equals(type.name())) {
            if (result.getMessages().get(WARNING) != null
                    && result.getMessages().get(WARNING).size() > 0) {
              return Boolean.FALSE;
            }
          }
        }
        return Boolean.TRUE;
      }

      @Override
      @Nonnull
      public String getMessage() {
        return message;
      }

      @Nonnull
      @Override
      public String getDetailedMessage(@Nonnull T model) {
        return detailedMessage;
      }

      @Override
      @Nonnull
      public ModelValidationMessageType getType() {
        return type;
      }
    };
  }

}