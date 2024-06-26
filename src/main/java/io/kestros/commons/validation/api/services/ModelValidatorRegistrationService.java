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

package io.kestros.commons.validation.api.services;

import io.kestros.commons.structuredslingmodels.BaseSlingModel;
import io.kestros.commons.validation.api.models.ModelValidator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Model Validator Registration Service.
 */
public interface ModelValidatorRegistrationService {


  /**
   * Model Type to register the validator to.
   *
   * @return Model Type to register the validator to.
   */
  @Nonnull
  Class<? extends BaseSlingModel> getModelType();

  /**
   * Returns the ModelValidators to register.
   *
   * @return the ModelValidators to register.
   */
  @Nonnull
  List<ModelValidator> getModelValidators();

  /**
   * Returns the ModelValidatorRegistrationHandlerService.
   *
   * @return the ModelValidatorRegistrationHandlerService.
   */
  @Nullable
  ModelValidatorRegistrationHandlerService getModelValidatorRegistrationHandlerService();

}
