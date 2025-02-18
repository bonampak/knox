/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.knox.gateway.topology.discovery.cm;

import com.cloudera.api.swagger.RolesResourceApi;
import org.apache.knox.gateway.config.GatewayConfig;
import org.apache.knox.gateway.i18n.messages.MessagesFactory;

import java.util.Collection;
import java.util.Collections;

import static org.apache.knox.gateway.config.GatewayConfig.CLOUDERA_MANAGER_SERVICE_DISCOVERY_ROLE_FETCH_STRATEGY_BY_ROLE;
import static org.apache.knox.gateway.config.GatewayConfig.CLOUDERA_MANAGER_SERVICE_DISCOVERY_ROLE_FETCH_STRATEGY_BY_SERVICE;

public class ServiceRoleCollectorBuilder {

    private Collection<String> excludedRoleTypes = Collections.emptySet();
    private String fetchStrategy = CLOUDERA_MANAGER_SERVICE_DISCOVERY_ROLE_FETCH_STRATEGY_BY_ROLE;
    private long pageSize = 500;
    private RolesResourceApi rolesResourceApi;
    private static final ClouderaManagerServiceDiscoveryMessages log =
            MessagesFactory.get(ClouderaManagerServiceDiscoveryMessages.class);

    public ServiceRoleCollectorBuilder(GatewayConfig config) {
        if (config != null) {
            fetchStrategy = config.getClouderaManagerServiceDiscoveryRoleFetchStrategy();
            pageSize = config.getClouderaManagerServiceDiscoveryRoleConfigPageSize();
            excludedRoleTypes = config.getClouderaManagerServiceDiscoveryExcludedRoleTypes();
        }
    }

    public ServiceRoleCollectorBuilder rolesResourceApi(RolesResourceApi rolesResourceApi) {
        this.rolesResourceApi = rolesResourceApi;
        return this;
    }

    public ServiceRoleCollector build() {
        if (rolesResourceApi == null) {
            throw new IllegalArgumentException("roles resource API must be set");
        }
        TypeNameFilter roleTypeNameFilter = new TypeNameFilter(excludedRoleTypes);
        String clientBasePath = rolesResourceApi.getApiClient().getBasePath();
        if (CLOUDERA_MANAGER_SERVICE_DISCOVERY_ROLE_FETCH_STRATEGY_BY_ROLE.equals(fetchStrategy)) {
            log.usingSimpleRoleStrategy(fetchStrategy, clientBasePath);
            return new ServiceRoleCollectorByRole(rolesResourceApi, roleTypeNameFilter);
        } else if (CLOUDERA_MANAGER_SERVICE_DISCOVERY_ROLE_FETCH_STRATEGY_BY_SERVICE.equals(fetchStrategy)) {
            log.usingRoleStrategyWithPageSize(fetchStrategy, pageSize, clientBasePath);
            return new ServiceRoleCollectorByService(rolesResourceApi, pageSize, roleTypeNameFilter);
        } else {
            log.usingSimpleRoleStrategyFallback(fetchStrategy, clientBasePath);
            return new ServiceRoleCollectorByRole(rolesResourceApi, roleTypeNameFilter);
        }
    }

}
