package io.kestros.commons.validation.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.kestros.commons.structuredslingmodels.BaseSlingModel;
import io.kestros.commons.validation.api.ModelValidationMessageType;

/**
 * Base interface for Model Validation rules.
 */
public abstract class ModelValidator<T extends BaseSlingModel> {


  @JsonIgnore
  public abstract Boolean isValidCheck(T model);

  /**
   * Message to be shown when the current validator is not valid.
   *
   * @return Message to be shown when the current validator is not valid.
   */
  public abstract String getMessage();

  public abstract String getDetailedMessage(T model);

  /**
   * The error level of the current validator.  Can be ERROR, WARNING or INFO.
   *
   * @return The error level of the current validator.
   */
  public abstract ModelValidationMessageType getType();
}