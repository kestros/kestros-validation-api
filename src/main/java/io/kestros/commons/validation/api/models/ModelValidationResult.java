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

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.kestros.commons.structuredslingmodels.BaseSlingModel;
import io.kestros.commons.validation.api.ModelValidationMessageType;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 * The results of all registered model validators on a specific model.
 */
public interface ModelValidationResult {

  /**
   * Returns the results of all registered validators.
   *
   * @return the results of all registered validators.
   */
  @Nonnull
  List<ValidatorResult> getResults();

  /**
   * Returns the model that was validated.
   *
   * @param <T> Model Type.
   *
   * @return the model that was validated.
   */
  @Nonnull
  @JsonIgnore
  <T extends BaseSlingModel> T getModel();

  /**
   * Returns the validators that were run on the model.
   *
   * @return the validators that were run on the model.
   */
  @Nonnull
  @JsonIgnore
  List<ModelValidator> getValidators();

  /**
   * Boolean logic To determine whether the current validator passes validation.
   *
   * @return Whether the current Validator is valid or not.
   */
  @JsonIgnore
  boolean isValid();

  /**
   * Returns a map of all validation messages.
   *
   * @return a map of all validation messages.
   */
  @Nonnull
  Map<ModelValidationMessageType, List<String>> getMessages();
}
