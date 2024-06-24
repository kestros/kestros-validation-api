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
import io.kestros.commons.validation.api.models.ModelValidator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 * Handles registration and unregistration of ModelValidators.
 */
public interface ModelValidatorRegistrationHandlerService extends ManagedService {

  /**
   * Returns a map of registered ModelValidators.
   *
   * @return a map of registered ModelValidators.
   */
  @Nonnull
  Map<Class, List<ModelValidator>> getRegisteredModelValidatorMap();

  /**
   * Registers all validators from all OSGI services.
   */
  void registerAllValidatorsFromAllServices();

  /**
   * Registers all validators from a specific OSGI service.
   *
   * @param registrationService OSGI service to register validators from.
   */
  void registerAllValidatorsFromService(
          @Nonnull ModelValidatorRegistrationService registrationService);

  /**
   * Unregisters all validators from a specific OSGI service.
   *
   * @param registrationService OSGI service to unregister validators from.
   */
  void unregisterAllValidatorsFromService(
          @Nonnull ModelValidatorRegistrationService registrationService);

  /**
   * Registers a list of ModelValidators to a specific Model type.
   *
   * @param modelValidators List of ModelValidators to register.
   * @param type Model type to register the validators to.
   */
  void registerValidators(@Nonnull final List<ModelValidator> modelValidators,
          @Nonnull final Class type);

  /**
   * Removes a list of ModelValidators from a specific Model type.
   *
   * @param modelValidators List of ModelValidators to remove.
   * @param type Model type to remove the validators from.
   */
  void removeValidators(@Nonnull final List<ModelValidator> modelValidators,
          @Nonnull final Class type);

}