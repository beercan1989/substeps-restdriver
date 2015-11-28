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

package uk.co.baconi.substeps.restdriver.properties;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public enum RestDriverSubstepsConfiguration {

    PROPERTIES("environment"); // uninstantiable

    /**
     * All properties under "substeps.driver", including environment specific if available.
     */
    private final Config properties;

    private final String baseUrl;
    private final int connectTimeout;
    private final int socketTimeout;
    private final String userAgent;
    private final Optional<String> proxy;


    RestDriverSubstepsConfiguration(final String environmentProperty) {

        //
        // All properties under "substeps.driver", including environment specific if available.
        //
        final String propertyBase = "substeps.driver";
        final Config systemProperties = ConfigFactory.systemProperties();
        if (systemProperties.hasPath(environmentProperty)) {
            final String environment = systemProperties.getString(environmentProperty);
            // Load properties using ${environment}.conf falling back on the normal Typesafe Config structure.
            properties = ConfigFactory.
                    parseResourcesAnySyntax(environment).
                    withFallback(ConfigFactory.load()).
                    getConfig(propertyBase);
        } else {
            // Load properties without environment specific configuration.
            properties = ConfigFactory.
                    load().
                    getConfig(propertyBase);
        }

        this.baseUrl = determineBaseURL(properties.getString("baseUrl"));

        this.connectTimeout = Long.valueOf(properties.getDuration("rest.connectTimeout", TimeUnit.MILLISECONDS)).intValue();
        this.socketTimeout = Long.valueOf(properties.getDuration("rest.socketTimeout", TimeUnit.MILLISECONDS)).intValue();
        this.userAgent = properties.getString("rest.userAgent");

        final String proxy = properties.getString("rest.proxy");
        this.proxy = proxy.isEmpty() ? Optional.empty() : Optional.ofNullable(proxy);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Optional<String> getProxy() {
        return proxy;
    }

    public Config getProperties() {
        return properties;
    }

    private String determineBaseURL(final String baseUrlProperty) {
        final String property = removeTrailingSlash(baseUrlProperty);

        final String baseUrl;
        if (!property.startsWith("http") && !property.startsWith("file://")) {
            baseUrl = removeTrailingSlash(new File(property).toURI().toString());
        } else {
            baseUrl = property;
        }

        return baseUrl;
    }

    private String removeTrailingSlash(final String string) {

        final String result;
        if (string.endsWith("/")) {
            result = string.substring(0, string.length() - 1);
        } else {
            result = string;
        }

        return result;
    }

}
