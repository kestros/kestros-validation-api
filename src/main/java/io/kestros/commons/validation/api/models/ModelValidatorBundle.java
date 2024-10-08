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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.kestros.commons.structuredslingmodels.BaseSlingModel;
import io.kestros.commons.validation.api.ModelValidationMessageType;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * ModelValidator that holds a set of ModelValidators.
 */
public abstract class ModelValidatorBundle<T extends BaseSlingModel> extends ModelValidator<T> {

  private final List<ModelValidator<T>> validators = new ArrayList();

  /**
   * Constructs ModelValidator that holds a set of ModelValidators.
   */


  @Nonnull
  @Override
  public Boolean isValidCheck(@Nonnull T model) {
    if (validators.isEmpty()) {
      registerValidators();
    }
    for (final ModelValidator validator : getValidators()) {
      if (isAllMustBeTrue() && !validator.isValidCheck(model)) {
        return Boolean.FALSE;
      } else if (!isAllMustBeTrue() && validator.isValidCheck(model)) {
        return Boolean.TRUE;
      }
    }
    return Boolean.TRUE;
  }

  /**
   * Returns a detailed message for the current bundle.
   *
   * @param model Model providing context for the detailed message.
   *
   * @return Detailed message for the current bundle.
   */
  @Nonnull
  public String getDetailedMessage(@Nonnull T model) {
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
  @SuppressFBWarnings("OPM_OVERLY_PERMISSIVE_METHOD")
  public void addValidator(@Nonnull final ModelValidator<T> validator) {
    validators.add(validator);
  }

  /**
   * Adds a list of ModelValidators to the bundle.
   *
   * @param validatorList ModelValidator List to add to the bundle.
   */
  public void addAllValidators(@Nonnull final List<ModelValidator<T>> validatorList) {
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
  @Nonnull
  public List<ModelValidator<T>> getValidators() {
    return new ArrayList<>(validators);
  }

  /**
   * Validation level of the current bundle.
   *
   * @return Validation level of the current bundle.
   */
  @SuppressFBWarnings("SLS_SUSPICIOUS_LOOP_SEARCH")
  @Nonnull
  public ModelValidationMessageType getType() {
    ModelValidationMessageType type = ModelValidationMessageType.INFO;
    for (ModelValidator validator : getValidators()) {
      if (validator.getType().name().equals(ModelValidationMessageType.ERROR.name())) {
        return ModelValidationMessageType.ERROR;
      } else if (validator.getType().name().equals(ModelValidationMessageType.WARNING.name())) {
        type = ModelValidationMessageType.WARNING;
      }
    }
    return type;
  }
}