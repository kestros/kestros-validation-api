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

import io.kestros.commons.validation.api.ModelValidationMessageType;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The results of a single Model Validator.
 */
public interface ValidatorResult {

  /**
   * Whether the processed validator is considered valid.
   *
   * @return Whether the processed validator is considered valid.
   */
  boolean isValid();

  /**
   * Message of the validation result.
   *
   * @return Message of the validation result.
   */
  @Nonnull
  String getMessage();

  /**
   * Detailed message of the validation result.
   *
   * @return Detailed message of the validation result.
   */
  @Nonnull
  String getDetailedMessage();

  /**
   * Sling Resource Type of the component that will provide documentation for the validation
   * message.
   *
   * @return Sling Resource Type of the component that will provide documentation for the
   *         validation message.
   */
  @Nullable
  String getDocumentationResourceType();

  /**
   * All bundle validators that were run as of the current validator.
   *
   * @return All bundle validators that were run as of the current validator.
   */
  @Nullable
  List<ValidatorResult> getBundled();

  /**
   * The class path of the validator that was run.
   *
   * @return the class path of the validator that was run.
   */
  @Nonnull
  String getValidatorClassPath();

  /**
   * The level of the validation message.
   *
   * @return the level of the validation message.
   */
  @Nonnull
  ModelValidationMessageType getType();

  /**
   * Returns a map of all validation messages.
   *
   * @return a map of all validation messages.
   */
  @Nonnull
  Map<ModelValidationMessageType, List<String>> getMessages();

}
