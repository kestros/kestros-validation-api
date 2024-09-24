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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import io.kestros.commons.structuredslingmodels.BaseResource;
import io.kestros.commons.validation.api.ModelValidationMessageType;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.junit.Before;
import org.junit.Test;

public class ModelValidatorBundleTest {

  private List<ModelValidator> modelValidatorList = new ArrayList<>();
  private ModelValidatorBundle modelValidatorBundle;
  private ModelValidator modelValidator1;
  private ModelValidator modelValidator2;
  private ModelValidator modelValidator3;
  private BaseResource model;

  @Before
  public void setUp() throws Exception {
    modelValidator1 = mock(ModelValidator.class);
    modelValidator2 = mock(ModelValidator.class);
    modelValidator3 = mock(ModelValidator.class);
    model = mock(BaseResource.class);
    modelValidatorBundle = spy(new ModelValidatorBundle() {
      @Override
      public void registerValidators() {

      }

      @Override
      public boolean isAllMustBeTrue() {
        return false;
      }

      @Nonnull
      @Override
      public String getMessage() {
        return null;
      }
    });
  }

  @Test
  public void testIsValidCheck() {
    when(modelValidator1.getType()).thenReturn(ModelValidationMessageType.ERROR);
    when(modelValidator2.getType()).thenReturn(ModelValidationMessageType.WARNING);
    when(modelValidator3.getType()).thenReturn(ModelValidationMessageType.INFO);
    modelValidatorList.add(modelValidator1);
    modelValidatorList.add(modelValidator2);
    modelValidatorList.add(modelValidator3);

    when(modelValidator1.isValidCheck(model)).thenReturn(true);
    when(modelValidator2.isValidCheck(model)).thenReturn(true);
    when(modelValidator3.isValidCheck(model)).thenReturn(true);

    modelValidatorBundle.addAllValidators(modelValidatorList);
    assertTrue(modelValidatorBundle.isValidCheck(model));
  }

  @Test
  public void testIsValidCheckWhenHasFalseAndNotAllMustBeTrue() {
    when(modelValidator1.getType()).thenReturn(ModelValidationMessageType.ERROR);
    when(modelValidator2.getType()).thenReturn(ModelValidationMessageType.WARNING);
    when(modelValidator3.getType()).thenReturn(ModelValidationMessageType.INFO);
    modelValidatorList.add(modelValidator1);
    modelValidatorList.add(modelValidator2);
    modelValidatorList.add(modelValidator3);

    when(modelValidator1.isValidCheck(model)).thenReturn(false);
    when(modelValidator2.isValidCheck(model)).thenReturn(true);
    when(modelValidator3.isValidCheck(model)).thenReturn(true);

    doReturn(false).when(modelValidatorBundle).isAllMustBeTrue();

    modelValidatorBundle.addAllValidators(modelValidatorList);
    assertTrue(modelValidatorBundle.isValidCheck(model));
  }

  @Test
  public void testIsValidCheckWhenHasFalseAndAllMustBeTrue() {
    when(modelValidator1.getType()).thenReturn(ModelValidationMessageType.ERROR);
    when(modelValidator2.getType()).thenReturn(ModelValidationMessageType.WARNING);
    when(modelValidator3.getType()).thenReturn(ModelValidationMessageType.INFO);
    modelValidatorList.add(modelValidator1);
    modelValidatorList.add(modelValidator2);
    modelValidatorList.add(modelValidator3);

    when(modelValidator1.isValidCheck(model)).thenReturn(false);
    when(modelValidator2.isValidCheck(model)).thenReturn(true);
    when(modelValidator3.isValidCheck(model)).thenReturn(true);

    doReturn(true).when(modelValidatorBundle).isAllMustBeTrue();

    modelValidatorBundle.addAllValidators(modelValidatorList);
    assertFalse(modelValidatorBundle.isValidCheck(model));
  }

  @Test
  public void testIsValidCheckWhenHasTrueAndAllMustBeTrue() {
    when(modelValidator1.getType()).thenReturn(ModelValidationMessageType.ERROR);
    when(modelValidator2.getType()).thenReturn(ModelValidationMessageType.WARNING);
    when(modelValidator3.getType()).thenReturn(ModelValidationMessageType.INFO);
    modelValidatorList.add(modelValidator1);
    modelValidatorList.add(modelValidator2);
    modelValidatorList.add(modelValidator3);

    when(modelValidator1.isValidCheck(model)).thenReturn(true);
    when(modelValidator2.isValidCheck(model)).thenReturn(true);
    when(modelValidator3.isValidCheck(model)).thenReturn(true);

    doReturn(true).when(modelValidatorBundle).isAllMustBeTrue();

    modelValidatorBundle.addAllValidators(modelValidatorList);
    assertTrue(modelValidatorBundle.isValidCheck(model));
  }

  @Test
  public void testIsValidCheckWhenHasTrueAndAllMustBeTrueIsFalse() {
    when(modelValidator1.getType()).thenReturn(ModelValidationMessageType.ERROR);
    when(modelValidator2.getType()).thenReturn(ModelValidationMessageType.WARNING);
    when(modelValidator3.getType()).thenReturn(ModelValidationMessageType.INFO);
    modelValidatorList.add(modelValidator1);
    modelValidatorList.add(modelValidator2);
    modelValidatorList.add(modelValidator3);

    when(modelValidator1.isValidCheck(model)).thenReturn(true);
    when(modelValidator2.isValidCheck(model)).thenReturn(true);
    when(modelValidator3.isValidCheck(model)).thenReturn(true);

    doReturn(false).when(modelValidatorBundle).isAllMustBeTrue();

    modelValidatorBundle.addAllValidators(modelValidatorList);
    assertTrue(modelValidatorBundle.isValidCheck(model));
  }

  @Test
  public void testGetDetailedMessage() {
  }

  @Test
  public void testGetType() {
    when(modelValidator1.getType()).thenReturn(ModelValidationMessageType.ERROR);
    when(modelValidator2.getType()).thenReturn(ModelValidationMessageType.WARNING);
    when(modelValidator3.getType()).thenReturn(ModelValidationMessageType.INFO);
    modelValidatorList.add(modelValidator1);
    modelValidatorList.add(modelValidator2);
    modelValidatorList.add(modelValidator3);

    modelValidatorBundle.addAllValidators(modelValidatorList);
    assertEquals(3, modelValidatorBundle.getValidators().size());
    assertEquals(ModelValidationMessageType.ERROR,
            modelValidatorBundle.getType());
  }

  @Test
  public void testGetTypeWhenNoError() {
    when(modelValidator2.getType()).thenReturn(ModelValidationMessageType.WARNING);
    when(modelValidator3.getType()).thenReturn(ModelValidationMessageType.INFO);
    modelValidatorList.add(modelValidator2);
    modelValidatorList.add(modelValidator3);

    modelValidatorBundle.addAllValidators(modelValidatorList);
    assertEquals(2, modelValidatorBundle.getValidators().size());
    assertEquals(ModelValidationMessageType.WARNING,
            modelValidatorBundle.getType());
  }


  @Test
  public void testGetTypeWhenNoErrorOrWarning() {
    when(modelValidator3.getType()).thenReturn(ModelValidationMessageType.INFO);
    modelValidatorList.add(modelValidator3);

    modelValidatorBundle.addAllValidators(modelValidatorList);
    assertEquals(1, modelValidatorBundle.getValidators().size());
    assertEquals(ModelValidationMessageType.INFO,
            modelValidatorBundle.getType());
  }

  @Test
  public void testGetTypeWhenEmpty() {

    modelValidatorBundle.addAllValidators(modelValidatorList);
    assertEquals(0, modelValidatorBundle.getValidators().size());
    assertEquals(ModelValidationMessageType.INFO,
            modelValidatorBundle.getType());
  }

  @Test
  public void testAddAllValidators() {
    modelValidatorList.add(modelValidator1);
    modelValidatorList.add(modelValidator2);
    modelValidatorList.add(modelValidator3);

    modelValidatorBundle.addAllValidators(modelValidatorList);
    assertEquals(3, modelValidatorBundle.getValidators().size());
  }
}