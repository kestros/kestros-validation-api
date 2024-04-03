package io.kestros.commons.validation.api.services;

import io.kestros.commons.structuredslingmodels.BaseSlingModel;
import io.kestros.commons.validation.api.models.ModelValidator;
import java.util.List;

/**
 * Model Validator Registration Service.
 */
public interface ModelValidatorRegistrationService {


  /**
   * Model Type to register the validator to.
   *
   * @return Model Type to register the validator to.
   */
  Class<? extends BaseSlingModel> getModelType();

  /**
   * Returns the ModelValidators to register.
   *
   * @return the ModelValidators to register.
   */
  List  <ModelValidator> getModelValidators();

  ModelValidatorRegistrationHandlerService getModelValidatorRegistrationHandlerService();

}
