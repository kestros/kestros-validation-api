package io.kestros.commons.validation.api.services;

import io.kestros.commons.osgiserviceutils.services.ManagedService;
import io.kestros.commons.validation.api.models.ModelValidator;
import java.util.List;
import java.util.Map;

public interface ModelValidatorRegistrationHandlerService extends ManagedService {

  Map<Class, List<ModelValidator>> getRegisteredModelValidatorMap();

  void registerAllValidatorsFromAllServices();

  void registerAllValidatorsFromService(ModelValidatorRegistrationService registrationService);

  void unregisterAllValidatorsFromService(ModelValidatorRegistrationService registrationService);

  void registerValidators(List<ModelValidator> modelValidators, Class type);

  void removeValidators(List<ModelValidator> modelValidators, Class type);


}
