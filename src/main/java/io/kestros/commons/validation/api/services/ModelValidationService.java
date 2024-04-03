package io.kestros.commons.validation.api.services;

import io.kestros.commons.osgiserviceutils.services.ManagedService;
import io.kestros.commons.structuredslingmodels.BaseResource;
import io.kestros.commons.validation.api.models.ModelValidationResult;

public interface ModelValidationService extends ManagedService {

  <T extends BaseResource> ModelValidationResult validate(T model);

}
