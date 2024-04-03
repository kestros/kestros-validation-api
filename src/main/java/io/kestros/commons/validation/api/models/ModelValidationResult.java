package io.kestros.commons.validation.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.kestros.commons.structuredslingmodels.BaseSlingModel;
import io.kestros.commons.validation.api.ModelValidationMessageType;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

public interface ModelValidationResult {

  @Nonnull
  List<ValidatorResult> getResults();

  @Nonnull
  @JsonIgnore
  <T extends BaseSlingModel> T getModel();

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

  @Nonnull
  Map<ModelValidationMessageType, List<String>> getMessages();
}
