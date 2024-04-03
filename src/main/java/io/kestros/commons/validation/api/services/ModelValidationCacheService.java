package io.kestros.commons.validation.api.services;

import io.kestros.commons.osgiserviceutils.exceptions.CacheRetrievalException;
import io.kestros.commons.osgiserviceutils.services.cache.CacheService;
import io.kestros.commons.osgiserviceutils.services.cache.ManagedCacheService;
import io.kestros.commons.structuredslingmodels.BaseResource;
import java.util.List;
import java.util.Map;
import org.apache.sling.api.resource.Resource;

public interface ModelValidationCacheService extends CacheService, ManagedCacheService {

  Map<String, Object> getCachedValidationMap();

  /**
   * Retrieves cached error messages for a specified resource, when adapted to the specified Class.
   *
   * @param resource Resource to cache validators for.
   * @param clazz Model class that the resource was validated against.
   * @param <T> extends BaseResource
   * @return List of validation errors as String.
   * @throws CacheRetrievalException Failed to retrieve a cached error message list.
   */
  <T extends BaseResource> List<String> getCachedErrorMessages(Resource resource, Class<T> clazz)
      throws CacheRetrievalException;

  /**
   * Retrieves cached warning messages for a specified resource, when adapted to the specified
   * Class.
   *
   * @param resource Resource to cache validators for.
   * @param clazz Model class that the resource was validated against.
   * @param <T> extends BaseResource
   * @return List of validation warnings as String.
   * @throws CacheRetrievalException Failed to retrieve a cached warning message list.
   */
  <T extends BaseResource> List<String> getCachedWarningMessages(Resource resource, Class<T> clazz)
      throws CacheRetrievalException;

  /**
   * Caches validators for a specified resource.
   *
   * @param model model to cache validators for.
   * @param <T> extends BaseResource
   */
  <T extends BaseResource> void cacheValidationResults(T model, List<String> errorMessages,
      List<String> warningMessages);

}
