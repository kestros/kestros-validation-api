package io.kestros.commons.validation.api.services;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;

public abstract class BaseModelValidationRegistrationService
    implements ModelValidatorRegistrationService {

  @Activate
  protected void activate() {
    if (getModelValidatorRegistrationHandlerService() != null) {
      getModelValidatorRegistrationHandlerService().registerAllValidatorsFromService(this);
    }
  }

  @Deactivate
  protected void deactivate() {
    if (getModelValidatorRegistrationHandlerService() != null) {
      getModelValidatorRegistrationHandlerService().unregisterAllValidatorsFromService(this);
    }
  }
}
