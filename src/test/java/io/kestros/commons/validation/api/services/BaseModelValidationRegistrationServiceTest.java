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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.kestros.commons.structuredslingmodels.BaseSlingModel;
import io.kestros.commons.validation.api.models.ModelValidator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class BaseModelValidationRegistrationServiceTest {

  @Rule
  public SlingContext context = new SlingContext();

  private BaseModelValidationRegistrationService baseModelValidationRegistrationService;
  private ModelValidatorRegistrationHandlerService modelValidatorRegistrationHandlerService;

  @Before
  public void setUp() throws Exception {
    baseModelValidationRegistrationService = create();
    modelValidatorRegistrationHandlerService = mock(ModelValidatorRegistrationHandlerService.class);
  }

  @Test
  public void testActivate() {
    baseModelValidationRegistrationService.activate();
    verify(modelValidatorRegistrationHandlerService, times(1)).registerAllValidatorsFromService(
            any());
  }

  @Test
  public void testDeactivate() {
    baseModelValidationRegistrationService.deactivate();
    verify(modelValidatorRegistrationHandlerService, times(1)).unregisterAllValidatorsFromService(
            any());
  }

  BaseModelValidationRegistrationService create() {
    return new BaseModelValidationRegistrationService() {
      @Nonnull
      @Override
      public Class<? extends BaseSlingModel> getModelType() {
        return null;
      }

      @Nonnull
      @Override
      public List<ModelValidator> getModelValidators() {
        return null;
      }

      @Nullable
      @Override
      public ModelValidatorRegistrationHandlerService getModelValidatorRegistrationHandlerService() {
        return modelValidatorRegistrationHandlerService;
      }
    };
  }
}