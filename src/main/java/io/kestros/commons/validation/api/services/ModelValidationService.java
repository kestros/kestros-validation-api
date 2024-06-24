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

import io.kestros.commons.osgiserviceutils.services.ManagedService;
import io.kestros.commons.structuredslingmodels.BaseResource;
import io.kestros.commons.validation.api.models.ModelValidationResult;
import javax.annotation.Nonnull;

/**
 * Model Validation Service.
 */
public interface ModelValidationService extends ManagedService {

  /**
   * Validates a model.
   * @param model Model to validate.
   * @return ModelValidationResult.
   * @param <T> Model Type.
   */
  @Nonnull
  <T extends BaseResource> ModelValidationResult validate(@Nonnull T model);

}
