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

package io.kestros.commons.validation.api.models;

import io.kestros.commons.structuredslingmodels.BaseSlingModel;
import io.kestros.commons.validation.api.ModelValidationMessageType;
import java.util.ArrayList;
import java.util.List;

/**
 * ModelValidator that holds a set of ModelValidators.
 */
public abstract class ModelValidatorBundle<T extends BaseSlingModel> extends ModelValidator<T> {

  private final List<ModelValidator<T>> validators = new ArrayList();

  /**
   * Constructs ModelValidator that holds a set of ModelValidators.
   */
  public ModelValidatorBundle() {
    registerValidators();
  }

  @Override
  public Boolean isValidCheck(T model) {
    if (validators.isEmpty()) {
      registerValidators();
    }
    for (final ModelValidator validator : getValidators()) {
      if (isAllMustBeTrue() && !validator.isValidCheck(model)) {
        return false;
      } else if (!isAllMustBeTrue() && validator.isValidCheck(model)) {
        return true;
      }
    }
    return isAllMustBeTrue();
  }

  public String getDetailedMessage(T model) {
    if (this.isAllMustBeTrue()) {
      return "All of the following are true:";
    } else {
      return "One of the following is true:";
    }
  }

  /**
   * Registers ModelValidators.
   */
  public abstract void registerValidators();

  /**
   * Whether ModelValidators assigned to the bundle must be true to be considered valid, or just
   * one.
   *
   * @return Whether ModelValidators assigned to the bundle must be true to be considered valid, or
   *         just one.
   */
  public abstract boolean isAllMustBeTrue();

  /**
   * Adds a validator to the bundle.
   *
   * @param validator ModelValidator to add to the bundle.
   */
  public void addValidator(final ModelValidator<T> validator) {
    validators.add(validator);
  }

  /**
   * Adds a list of ModelValidators to the bundle.
   *
   * @param validatorList ModelValidator List to add to the bundle.
   */
  public void addAllValidators(final List<ModelValidator<T>> validatorList) {
    if (validatorList == null) {
      return;
    }
    for (ModelValidator<T> validator : validatorList) {
      addValidator(validator);
    }
  }

  /**
   * List of all ModelsValidators in the bundle.
   *
   * @return List of all ModelsValidators in the bundle.
   */
  public List<ModelValidator<T>> getValidators() {
    return validators;
  }

  public ModelValidationMessageType getType() {
    ModelValidationMessageType type = ModelValidationMessageType.INFO;
    for (ModelValidator validator : getValidators()) {
      if (validator.getType().equals(ModelValidationMessageType.ERROR)) {
        return ModelValidationMessageType.ERROR;
      } else if (validator.getType().equals(ModelValidationMessageType.WARNING)) {
        type = ModelValidationMessageType.WARNING;
      }
    }
    return type;
  }
}