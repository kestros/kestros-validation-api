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

import static org.junit.Assert.*;

import io.kestros.commons.structuredslingmodels.BaseSlingModel;
import io.kestros.commons.validation.api.ModelValidationMessageType;
import javax.annotation.Nonnull;
import org.junit.Before;
import org.junit.Test;

public class DocumentedModelValidatorTest {

  private DocumentedModelValidator documentedModelValidator;
  @Before
  public void setUp() throws Exception {
    documentedModelValidator = new DocumentedModelValidator() {
      @Nonnull
      @Override
      public Boolean isValidCheck(@Nonnull BaseSlingModel model) {
        return null;
      }

      @Nonnull
      @Override
      public String getMessage() {
        return null;
      }

      @Nonnull
      @Override
      public String getDetailedMessage(@Nonnull BaseSlingModel model) {
        return null;
      }

      @Nonnull
      @Override
      public ModelValidationMessageType getType() {
        return null;
      }

      @Nonnull
      @Override
      public String getResourceType() {
        return "resourceType";
      }
    };
  }

  @Test
  public void testGetResourceType() {
    assertEquals("resourceType", documentedModelValidator.getResourceType());
  }
}