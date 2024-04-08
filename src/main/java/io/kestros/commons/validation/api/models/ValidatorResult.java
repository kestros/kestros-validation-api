package io.kestros.commons.validation.api.models;

import io.kestros.commons.validation.api.ModelValidationMessageType;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ValidatorResult {

  boolean isValid();

  @Nonnull
  String getMessage();

  @Nonnull
  String getDetailedMessage();

  @Nullable
  String getDocumentationResourceType();

  @Nullable
  List<ValidatorResult> getBundled();

  @Nonnull
  String getValidatorClassPath();

  ModelValidationMessageType getType();

  @Nonnull
  Map<ModelValidationMessageType, List<String>> getMessages();

}
