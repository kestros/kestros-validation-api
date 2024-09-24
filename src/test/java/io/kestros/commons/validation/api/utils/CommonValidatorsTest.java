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
import static io.kestros.commons.validation.api.ModelValidationMessageType.INFO;
import static io.kestros.commons.validation.api.ModelValidationMessageType.WARNING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.kestros.commons.structuredslingmodels.BaseResource;
import io.kestros.commons.validation.api.ModelValidationMessageType;
import io.kestros.commons.validation.api.models.ModelValidationResult;
import io.kestros.commons.validation.api.services.ModelValidationService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.sling.api.resource.Resource;
import org.junit.Before;
import org.junit.Test;

public class CommonValidatorsTest {

  private BaseResource model;
  private ModelValidationResult modelValidationResult;
  private List list;
  private ModelValidationService modelValidationService;
  private Map<ModelValidationMessageType, List<String>> validationResultMap = new HashMap<>();
  private Resource modelResource;
  private BaseResource model1;
  private BaseResource model2;
  private BaseResource model3;

  @Before
  public void setUp() throws Exception {
    modelValidationService = mock(ModelValidationService.class);
    model = mock(BaseResource.class);
    modelResource = mock(Resource.class);

    when(model.getResource()).thenReturn(modelResource);

  }

  @Test
  public void testHasTitle() {
    when(model.getTitle()).thenReturn("title");
    when(model.getName()).thenReturn("name");
    assertEquals("Title is configured.", CommonValidators.hasTitle().getMessage());
    assertEquals(ModelValidationMessageType.ERROR, CommonValidators.hasTitle().getType());
    assertEquals("The jcr:title property must be configured.",
            CommonValidators.hasTitle().getDetailedMessage(model));
    assertTrue(CommonValidators.hasTitle().isValidCheck(model));
  }

  @Test
  public void testHasDescription() {
    when(model.getDescription()).thenReturn("description");

    assertEquals("Description is configured.",
            CommonValidators.hasDescription(INFO).getMessage());
    assertEquals(INFO,
            CommonValidators.hasDescription(INFO).getType());
    assertEquals("The jcr:description property must be configured.",
            CommonValidators.hasDescription(INFO)
                    .getDetailedMessage(model));
    assertTrue(
            CommonValidators.hasDescription(INFO).isValidCheck(model));
  }

  @Test
  public void testHasFileExtension() {
    when(model.getName()).thenReturn("name.txt");
    when(modelResource.getName()).thenReturn("name.txt");
    assertEquals("Resource name ends with txt extension.",
            CommonValidators.hasFileExtension("txt", INFO).getMessage());
    assertEquals(INFO,
            CommonValidators.hasFileExtension("txt", INFO).getType());
    assertEquals("Filename name.txt is expected to end with .txt.",
            CommonValidators.hasFileExtension("txt", INFO)
                    .getDetailedMessage(model));
    assertTrue(
            CommonValidators.hasFileExtension("txt", INFO)
                    .isValidCheck(model));
  }

  @Test
  public void testHasChildResource() {
    assertEquals("Has child resource 'childResource'.",
            CommonValidators.hasChildResource("childResource", INFO)
                    .getMessage());
    assertEquals(INFO,
            CommonValidators.hasChildResource("childResource", INFO)
                    .getType());
    assertEquals("Expected child resource 'childResource' was not found.",
            CommonValidators.hasChildResource("childResource", INFO)
                    .getDetailedMessage(model));
    assertFalse(
            CommonValidators.hasChildResource("childResource", INFO)
                    .isValidCheck(model));
  }

  @Test
  public void testIsChildResourceValidResourceType() {
    assertEquals("Has valid child resource 'childResource'.",
            CommonValidators.isChildResourceValidResourceType("childResource", BaseResource.class,
                    INFO).getMessage());
    assertEquals(INFO,
            CommonValidators.isChildResourceValidResourceType("childResource", BaseResource.class,
                    INFO).getType());
    assertEquals(
            "Child resource 'childResource' could not be adapted to BaseResource. Likely the "
                    + "wrong resourceType.",
            CommonValidators.isChildResourceValidResourceType("childResource", BaseResource.class,
                    INFO).getDetailedMessage(model));
    assertTrue(
            CommonValidators.isChildResourceValidResourceType("childResource", BaseResource.class,
                    INFO).isValidCheck(model));
  }

  @Test
  public void testHasValidChild() {
    assertEquals("Has valid child BaseResource 'childResource'",
            CommonValidators.hasValidChild("childResource", BaseResource.class,
                    INFO).getMessage());
    assertEquals(INFO,
            CommonValidators.hasValidChild("childResource", BaseResource.class,
                    INFO).getType());
    assertEquals("All of the following are true:",
            CommonValidators.hasValidChild("childResource", BaseResource.class,
                    INFO).getDetailedMessage(model));
    assertFalse(
            CommonValidators.hasValidChild("childResource", BaseResource.class,
                    INFO).isValidCheck(model));
  }

  @Test
  public void testGetFailedErrorValidators() {
    when(model.getPath()).thenReturn("/path");
    validationResultMap.put(ModelValidationMessageType.ERROR,
            List.of("error1", "error2", "error3"));

    modelValidationResult = mock(ModelValidationResult.class);
    when(modelValidationResult.getMessages()).thenReturn(validationResultMap);
    assertEquals(3, CommonValidators.getFailedErrorValidators(model, modelValidationResult).size());
    assertEquals("Error validator failed for /path: error1",
            CommonValidators.getFailedErrorValidators(model, modelValidationResult).get(0)
                    .getMessage());
    assertEquals(ERROR,
            CommonValidators.getFailedErrorValidators(model, modelValidationResult).get(0)
                    .getType());
    assertEquals("", CommonValidators.getFailedErrorValidators(model, modelValidationResult).get(0)
            .getDetailedMessage(model));
    assertFalse(
            CommonValidators.getFailedErrorValidators(model, modelValidationResult).get(0)
                    .isValidCheck(model));
  }

  @Test
  public void testGetFailedWarningValidators() {
    when(model.getPath()).thenReturn("/path");
    validationResultMap.put(ModelValidationMessageType.WARNING,
            List.of("error1", "error2", "error3"));

    modelValidationResult = mock(ModelValidationResult.class);
    when(modelValidationResult.getMessages()).thenReturn(validationResultMap);
    assertEquals(3,
            CommonValidators.getFailedWarningValidators(model, modelValidationResult).size());
    assertEquals("Warning validator failed for /path: error1",
            CommonValidators.getFailedWarningValidators(model, modelValidationResult).get(0)
                    .getMessage());
    assertEquals(WARNING,
            CommonValidators.getFailedWarningValidators(model, modelValidationResult).get(0)
                    .getType());
    assertEquals("",
            CommonValidators.getFailedWarningValidators(model, modelValidationResult).get(0)
                    .getDetailedMessage(model));

    assertFalse(
            CommonValidators.getFailedWarningValidators(model, modelValidationResult).get(0)
                    .isValidCheck(model));
  }

  @Test
  public void testListContainsNoNulls() {
    list = new ArrayList();
    list.add("item1");
    assertEquals("message",
            CommonValidators.listContainsNoNulls(list, "message", ModelValidationMessageType.ERROR)
                    .getMessage());
    assertEquals(ERROR,
            CommonValidators.listContainsNoNulls(list, "message", ModelValidationMessageType.ERROR)
                    .getType());
    assertEquals("message",
            CommonValidators.listContainsNoNulls(list, "message", ModelValidationMessageType.ERROR)
                    .getDetailedMessage(model));
    assertTrue(
            CommonValidators.listContainsNoNulls(list, "message", ModelValidationMessageType.ERROR)
                    .isValidCheck(model));
  }

  @Test
  public void testListContainsNoNullsWhenHasNull() {
    list = new ArrayList();
    list.add(null);
    assertEquals("message",
            CommonValidators.listContainsNoNulls(list, "message", ModelValidationMessageType.ERROR)
                    .getMessage());
    assertEquals(ERROR,
            CommonValidators.listContainsNoNulls(list, "message", ModelValidationMessageType.ERROR)
                    .getType());
    assertEquals("message",
            CommonValidators.listContainsNoNulls(list, "message", ModelValidationMessageType.ERROR)
                    .getDetailedMessage(model));
    assertFalse(
            CommonValidators.listContainsNoNulls(list, "message", ModelValidationMessageType.ERROR)
                    .isValidCheck(model));
  }

  @Test
  public void testModelListHasNoErrors() {
    list = new ArrayList();
    assertEquals("message",
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).getMessage());
    assertEquals(ERROR,
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).getType());
    assertEquals("detailed-message",
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).getDetailedMessage(model));
    assertTrue(
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).isValidCheck(model));
  }

  @Test
  public void testModelListHasNoErrorsWhenHasErrors() {
    list = new ArrayList();
    list.add(model1);
    list.add(model2);
    list.add(model3);

    modelValidationResult = mock(ModelValidationResult.class);
    when(modelValidationResult.getMessages()).thenReturn(validationResultMap);
    validationResultMap.put(ERROR, Arrays.asList("error"));
    validationResultMap.put(WARNING, Arrays.asList("warning1"));
    when(modelValidationService.validate(model1)).thenReturn(modelValidationResult);

    assertEquals("message",
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).getMessage());
    assertEquals(ERROR,
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).getType());
    assertEquals("detailed-message",
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).getDetailedMessage(model));
    assertFalse(
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).isValidCheck(model));
  }


  @Test
  public void testModelListHasNoErrorsWhenHasWarnings() {
    list = new ArrayList();
    list.add(model1);
    list.add(model2);
    list.add(model3);

    modelValidationResult = mock(ModelValidationResult.class);
    when(modelValidationResult.getMessages()).thenReturn(validationResultMap);
    validationResultMap.put(WARNING, Arrays.asList("warning1"));
    when(modelValidationService.validate(model1)).thenReturn(modelValidationResult);


    assertEquals("message",
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).getMessage());
    assertEquals(ERROR,
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).getType());
    assertEquals("detailed-message",
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).getDetailedMessage(model));
    assertTrue(
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).isValidCheck(model));
  }

  @Test
  public void testModelListHasNoErrorsWhenHasErrorsAndWarnings() {
    list = new ArrayList();
    list.add(model1);
    list.add(model2);
    list.add(model3);

    modelValidationResult = mock(ModelValidationResult.class);
    when(modelValidationResult.getMessages()).thenReturn(validationResultMap);
    validationResultMap.put(ERROR, Arrays.asList("error"));
    validationResultMap.put(WARNING, Arrays.asList("warning1"));
    when(modelValidationService.validate(model1)).thenReturn(modelValidationResult);

    assertEquals("message",
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).getMessage());
    assertEquals(ERROR,
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).getType());
    assertEquals("detailed-message",
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).getDetailedMessage(model));
    assertFalse(
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).isValidCheck(model));
  }

  @Test
  public void testModelListHasNoErrorsWhenWarningListIsNull() {
    list = new ArrayList();
    list.add(model1);
    list.add(model2);
    list.add(model3);

    modelValidationResult = mock(ModelValidationResult.class);
    when(modelValidationResult.getMessages()).thenReturn(validationResultMap);
    validationResultMap.put(ERROR, null);
    when(modelValidationService.validate(model1)).thenReturn(modelValidationResult);

    assertEquals("message",
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).getMessage());
    assertEquals(ERROR,
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).getType());
    assertEquals("detailed-message",
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).getDetailedMessage(model));
    assertTrue(
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).isValidCheck(model));
  }

  @Test
  public void testModelListHasNoErrorsWhenWarningListIsEmpty() {
    list = new ArrayList();
    list.add(model1);
    list.add(model2);
    list.add(model3);

    modelValidationResult = mock(ModelValidationResult.class);
    when(modelValidationResult.getMessages()).thenReturn(validationResultMap);
    validationResultMap.put(ERROR, Collections.emptyList());
    when(modelValidationService.validate(model1)).thenReturn(modelValidationResult);

    assertEquals("message",
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).getMessage());
    assertEquals(ERROR,
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).getType());
    assertEquals("detailed-message",
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).getDetailedMessage(model));
    assertTrue(
            CommonValidators.modelListHasNoErrors(list, "message", "detailed-message",
                    modelValidationService).isValidCheck(model));
  }

  @Test
  public void testModelListHasNoWarnings() {
    list = new ArrayList();
    assertEquals("message",
            CommonValidators.modelListHasNoWarnings(list, "message", "detailed-message",
                    modelValidationService).getMessage());
    assertEquals(WARNING,
            CommonValidators.modelListHasNoWarnings(list, "message", "detailed-message",
                    modelValidationService).getType());
    assertEquals("detailed-message",
            CommonValidators.modelListHasNoWarnings(list, "message", "detailed-message",
                    modelValidationService).getDetailedMessage(model));
    assertTrue(
            CommonValidators.modelListHasNoWarnings(list, "message", "detailed-message",
                    modelValidationService).isValidCheck(model));
  }

  @Test
  public void testModelListHasNoWarningsWhenWarningListIsNull() {
    list = new ArrayList();
    list.add(model1);
    list.add(model2);
    list.add(model3);

    modelValidationResult = mock(ModelValidationResult.class);
    when(modelValidationResult.getMessages()).thenReturn(validationResultMap);
    validationResultMap.put(WARNING, null);
    when(modelValidationService.validate(model1)).thenReturn(modelValidationResult);

    assertEquals("message",
            CommonValidators.modelListHasNoWarnings(list, "message", "detailed-message",
                    modelValidationService).getMessage());
    assertEquals(WARNING,
            CommonValidators.modelListHasNoWarnings(list, "message", "detailed-message",
                    modelValidationService).getType());
    assertEquals("detailed-message",
            CommonValidators.modelListHasNoWarnings(list, "message", "detailed-message",
                    modelValidationService).getDetailedMessage(model));
    assertTrue(
            CommonValidators.modelListHasNoWarnings(list, "message", "detailed-message",
                    modelValidationService).isValidCheck(model));
  }

  @Test
  public void testModelListHasNoWarningsWhenWarningListIsEmpty() {
    list = new ArrayList();
    list.add(model1);
    list.add(model2);
    list.add(model3);

    modelValidationResult = mock(ModelValidationResult.class);
    when(modelValidationResult.getMessages()).thenReturn(validationResultMap);
    validationResultMap.put(WARNING, Collections.emptyList());
    when(modelValidationService.validate(model1)).thenReturn(modelValidationResult);

    assertEquals("message",
            CommonValidators.modelListHasNoWarnings(list, "message", "detailed-message",
                    modelValidationService).getMessage());
    assertEquals(WARNING,
            CommonValidators.modelListHasNoWarnings(list, "message", "detailed-message",
                    modelValidationService).getType());
    assertEquals("detailed-message",
            CommonValidators.modelListHasNoWarnings(list, "message", "detailed-message",
                    modelValidationService).getDetailedMessage(model));
    assertTrue(
            CommonValidators.modelListHasNoWarnings(list, "message", "detailed-message",
                    modelValidationService).isValidCheck(model));
  }

  @Test
  public void testModelListHasNoWarningsWhenHasWarnings() {
    list = new ArrayList();
    list.add(model1);
    list.add(model2);
    list.add(model3);

    modelValidationResult = mock(ModelValidationResult.class);
    when(modelValidationResult.getMessages()).thenReturn(validationResultMap);
    validationResultMap.put(WARNING, Arrays.asList("warning1"));
    when(modelValidationService.validate(model1)).thenReturn(modelValidationResult);


    assertEquals("message",
            CommonValidators.modelListHasNoWarnings(list, "message", "detailed-message",
                    modelValidationService).getMessage());
    assertEquals(WARNING,
            CommonValidators.modelListHasNoWarnings(list, "message", "detailed-message",
                    modelValidationService).getType());
    assertEquals("detailed-message",
            CommonValidators.modelListHasNoWarnings(list, "message", "detailed-message",
                    modelValidationService).getDetailedMessage(model));
    assertFalse(
            CommonValidators.modelListHasNoWarnings(list, "message", "detailed-message",
                    modelValidationService).isValidCheck(model));
  }

  @Test
  public void testModelListHasNoInfo() {
    list = new ArrayList();
    list.add(model1);
    list.add(model2);
    list.add(model3);

    modelValidationResult = mock(ModelValidationResult.class);
    when(modelValidationResult.getMessages()).thenReturn(validationResultMap);
    validationResultMap.put(WARNING, Arrays.asList("warning1"));
    when(modelValidationService.validate(model1)).thenReturn(modelValidationResult);


    assertEquals("message",
            CommonValidators.modelListHasNoFailedValidatorsOfType(list, "message",
                    "detailed-message",
                    INFO, modelValidationService).getMessage());
    assertEquals(INFO,
            CommonValidators.modelListHasNoFailedValidatorsOfType(list, "message",
                    "detailed-message",
                    INFO, modelValidationService).getType());
    assertEquals("detailed-message",
            CommonValidators.modelListHasNoFailedValidatorsOfType(list, "message",
                    "detailed-message",
                    INFO, modelValidationService).getDetailedMessage(model));
    assertTrue(
            CommonValidators.modelListHasNoFailedValidatorsOfType(list, "message",
                    "detailed-message",
                    INFO, modelValidationService).isValidCheck(model));
  }
}