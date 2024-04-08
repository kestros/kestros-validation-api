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
public class CommonValidators {

  /**
   * Validator that checks if the current Resource has a title value.
   *
   * @param <T> Generic model type.
   *
   * @return Validator that checks if the current Resource has a title value.
   */
  public static <T extends BaseResource> ModelValidator hasTitle() {
    return new ModelValidator<T>() {


      @Override
      public Boolean isValidCheck(T model) {
        if (model instanceof BaseResource) {
          BaseResource resource = (BaseResource) model;
          if (resource != null) {
            return !resource.getName().equals(resource.getTitle()) && StringUtils.isNotEmpty(
                    resource.getTitle());
          }
        }
        return false;
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
  public static <T extends BaseResource> ModelValidator hasDescription(
          final ModelValidationMessageType messageType) {
    return new ModelValidator<T>() {

      @Override
      public Boolean isValidCheck(T model) {
        if (model instanceof BaseResource) {
          BaseResource resource = (BaseResource) model;
          if (model != null) {
            return StringUtils.isNotEmpty(resource.getDescription());
          }
        }
        return false;
      }

      @Override
      public String getMessage() {
        return "Description is configured.";
      }

      @Override
      public String getDetailedMessage(T model) {
        return "The jcr:description property must be configured.";
      }

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
  public static <T extends BaseResource> ModelValidator hasFileExtension(final String extension,
          final ModelValidationMessageType messageType) {
    return new ModelValidator<T>() {

      @Override
      public Boolean isValidCheck(T model) {
        if (model instanceof BaseResource) {
          BaseResource resource = (BaseResource) model;
          if (resource != null) {
            return resource.getName().endsWith(extension);
          }
        }
        return false;
      }

      @Override
      public String getMessage() {
        return "Resource name ends with " + extension + " extension.";
      }

      @Override
      public String getDetailedMessage(T model) {
        if (model != null) {
          return String.format("Filename %s is expected to end with .%s.",
                  model.getResource().getName(), extension);
        } else {
          return String.format("Filename is expected to end with .%s.", extension);
        }
      }

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
   *
   * @return Validator that checks if a specified child Resource exists.
   */
  public static <T extends BaseResource> ModelValidator hasChildResource(final String childName,
          ModelValidationMessageType type) {
    return new ModelValidator<T>() {

      @Override
      public Boolean isValidCheck(T model) {
        try {
          SlingModelUtils.getChildAsBaseResource(childName, model.getResource());
        } catch (final ChildResourceNotFoundException exception) {
          return false;
        }
        return true;
      }

      @Override
      public String getMessage() {
        return String.format("Has child resource '%s'.", childName);
      }

      @Override
      public String getDetailedMessage(T model) {
        // todo this
        return "";
      }

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
   * @param childType type of Model to attempt to adapt the child to. Must extend BaseResource.
   * @param <T> Generic model type.
   * @param <S> Generic model type.
   *
   * @return Validator that checks if a specified child does not fail any ERROR type validators.
   */
  public static <T extends BaseResource, S extends BaseResource>
  ModelValidator isChildResourceValidResourceType(
          final String childName, final Class<S> childType, ModelValidationMessageType type) {

    return new ModelValidator<T>() {

      @Override
      public Boolean isValidCheck(T model) {
        try {
          SlingModelUtils.getChildAsType(childName, model.getResource(), childType);
        } catch (InvalidResourceTypeException e) {
          return false;
        } catch (ChildResourceNotFoundException e) {
          return true;
        }
        return true;
      }

      @Override
      public String getMessage() {
        return String.format("Has valid child resource '%s'.", childName);
      }

      @Override
      public String getDetailedMessage(T model) {
        return null;
      }

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
   * @param childType type of Model to attempt to adapt the child to. Must extend BaseResource.
   * @param <T> Generic model type.
   * @param <S> Generic model type.
   *
   * @return Model Validator Bundle that checks if a specified child resource exists, and is valid.
   */
  public static <T extends BaseResource, S extends BaseResource> ModelValidatorBundle<T> hasValidChild(
          final String childName, final Class<S> childType,
          final ModelValidationMessageType messageType) {
    return new ModelValidatorBundle<T>() {
      @Override
      public String getMessage() {
        return "Has valid child " + childType.getSimpleName() + " '" + childName + "'";
      }

      @Override
      public void registerValidators() {
        addValidator(hasChildResource(childName, messageType));
        addValidator(isChildResourceValidResourceType(childName, childType, messageType));
      }

      @Override
      public ModelValidationMessageType getType() {
        return messageType;
      }

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
   *
   * @return List of failed Error validators from a model.
   */
  public static <T extends BaseResource> List<ModelValidator> getFailedErrorValidators(
          @Nonnull final T model, ModelValidationResult modelValidationResult) {
    final List<ModelValidator> errorValidators = new ArrayList<>();
    for (final String errorMessage : modelValidationResult.getMessages().get(ERROR)) {
      final ModelValidator validator = new ModelValidator<T>() {


        @Override
        public Boolean isValidCheck(T model) {
          return false;
        }

        @Override
        public String getMessage() {
          return "Error validator failed for " + model.getPath() + ": " + errorMessage;
        }

        @Override
        public String getDetailedMessage(T model) {
          return StringUtils.EMPTY;
        }

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
   *
   * @return List of failed Error Warning from a model.
   */
  public static <T extends BaseResource> List<ModelValidator> getFailedWarningValidators(
          final T model, ModelValidationResult modelValidationResult) {
    final List<ModelValidator> warningValidators = new ArrayList<>();

    for (final String warningMessage : modelValidationResult.getMessages().get(
            WARNING)) {
      final ModelValidator validator = new ModelValidator<T>() {

        @Override
        public Boolean isValidCheck(T model) {
          return false;
        }

        @Override
        public String getMessage() {
          return "Warning validator failed for " + model.getPath() + ": " + warningMessage;
        }

        @Override
        public String getDetailedMessage(T model) {
          return "";
        }

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
  public static <T> ModelValidator listContainsNoNulls(@Nonnull final List<T> list,
          @Nonnull final String message, @Nonnull final ModelValidationMessageType type) {
    return new ModelValidator() {

      @Override
      public Boolean isValidCheck(BaseSlingModel model) {
        for (final Object object : list) {
          if (object == null) {
            return false;
          }
        }
        return true;
      }

      @Override
      public String getMessage() {
        return message;
      }

      @Override
      public String getDetailedMessage(BaseSlingModel model) {
        // todo this
        return "";
      }

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
   *
   * @return Validates whether a specified list of models has any error messages.
   */
  public static <T extends BaseResource> ModelValidator modelListHasNoErrors(List<T> modelList,
          String message, String detailedMessage,
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
   *
   * @return Validates whether a specified list of models has any warning messages.
   */
  public static <T extends BaseResource> ModelValidator modelListHasNoWarnings(List<T> modelList,
          String message, String detailedMessage,
          @Nonnull ModelValidationService modelValidationService) {
    return modelListHasNoFailedValidatorsOfType(modelList, message, detailedMessage, WARNING,
            modelValidationService);
  }

  private static <T extends BaseResource> ModelValidator modelListHasNoFailedValidatorsOfType(
          List<T> modelList, String message, String detailedMessage,
          ModelValidationMessageType type,
          ModelValidationService validationService) {
    return new ModelValidator<T>() {

      @Override
      public Boolean isValidCheck(T model) {
        for (T model1 : modelList) {
          ModelValidationResult result = validationService.validate(model1);
          if (ERROR.equals(type)) {
            if (result.getMessages().get(ERROR).size() > 0) {
              return false;
            }
            if (result.getMessages().get(WARNING).size() > 0) {
              return false;
            }
          }
        }
        return true;
      }

      @Override
      public String getMessage() {
        return message;
      }

      @Override
      public String getDetailedMessage(T model) {
        return detailedMessage;
      }

      @Override
      public ModelValidationMessageType getType() {
        return type;
      }
    };
  }

}