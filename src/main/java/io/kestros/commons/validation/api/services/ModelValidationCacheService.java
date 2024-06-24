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

package io.kestros.commons.validation.api.services;

import io.kestros.commons.osgiserviceutils.exceptions.CacheRetrievalException;
import io.kestros.commons.osgiserviceutils.services.cache.CacheService;
import io.kestros.commons.osgiserviceutils.services.cache.ManagedCacheService;
import io.kestros.commons.structuredslingmodels.BaseResource;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import org.apache.sling.api.resource.Resource;

/**
 * Model Validation Cache Service.
 */
public interface ModelValidationCacheService extends CacheService, ManagedCacheService {

  /**
   * Retrieves the cached validation map.
   *
   * @return Cached validation map.
   */
  @Nonnull
  Map<String, Object> getCachedValidationMap();

  /**
   * Retrieves cached error messages for a specified resource, when adapted to the specified Class.
   *
   * @param resource Resource to cache validators for.
   * @param clazz Model class that the resource was validated against.
   * @param <T> extends BaseResource
   *
   * @return List of validation errors as String.
   *
   * @throws CacheRetrievalException Failed to retrieve a cached error message list.
   */
  @Nonnull
  <T extends BaseResource> List<String> getCachedErrorMessages(@Nonnull Resource resource,
          @Nonnull Class<T> clazz) throws CacheRetrievalException;

  /**
   * Retrieves cached warning messages for a specified resource, when adapted to the specified
   * Class.
   *
   * @param resource Resource to cache validators for.
   * @param clazz Model class that the resource was validated against.
   * @param <T> extends BaseResource
   *
   * @return List of validation warnings as String.
   *
   * @throws CacheRetrievalException Failed to retrieve a cached warning message list.
   */
  @Nonnull
  <T extends BaseResource> List<String> getCachedWarningMessages(@Nonnull Resource resource,
          @Nonnull Class<T> clazz) throws CacheRetrievalException;

  /**
   * Caches validators for a specified resource.
   *
   * @param model model to cache validators for.
   * @param <T> extends BaseResource
   * @param errorMessages List of error messages to cache.
   * @param warningMessages List of warning messages to cache.
   */
  @Nonnull
  <T extends BaseResource> void cacheValidationResults(@Nonnull T model,
          @Nonnull List<String> errorMessages, @Nonnull List<String> warningMessages);

}
