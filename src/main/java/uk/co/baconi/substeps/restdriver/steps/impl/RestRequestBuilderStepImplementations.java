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

package uk.co.baconi.substeps.restdriver.steps.impl;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.technophobia.substeps.model.Scope;
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepImplementations;
import com.technophobia.substeps.model.SubSteps.StepParameter;
import org.apache.http.client.methods.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.baconi.substeps.restdriver.RestDriverSetupAndTearDown;
import uk.co.baconi.substeps.restdriver.builders.GroupedKeyPairRequestBodyEntry;
import uk.co.baconi.substeps.restdriver.builders.RequestBodyBuilder;
import uk.co.baconi.substeps.restdriver.builders.RequestBodyEntry;
import uk.co.baconi.substeps.restdriver.converters.RequestBodyBuilderConverter;
import uk.co.baconi.substeps.restdriver.converters.ScopeConverter;
import uk.co.baconi.substeps.restdriver.properties.RestDriverSubstepsConfiguration;
import uk.co.baconi.substeps.restdriver.steps.AbstractRestDriverSubStepImplementations;

import java.io.IOException;
import java.util.List;

@StepImplementations(requiredInitialisationClasses = RestDriverSetupAndTearDown.class)
public class RestRequestBuilderStepImplementations extends AbstractRestDriverSubStepImplementations {

    private static final Logger LOG = LoggerFactory.getLogger(RestRequestBuilderStepImplementations.class);

    /**
     * Setups up a new rest request and throws away any that are in the current scenario scope.
     *
     * @example RestRequest setup new request
     * @section Rest Builder
     */
    @Step("RestRequest setup new request")
    public void restRequestSetupNewRequest() {
        setRequest(createNewRequest());

        // TODO - Consider resetting other parts?
    }

    /**
     * Create a new rest request using the given HTTP method and URL. The URL can either be absolute or relative to the
     * base url in the properties.
     *
     * @param method the HTTP method type to create the request as.
     * @param url    the URL where the request will be sent to.
     * @throws IOException if the response cannot be saved.
     * @example NewRestRequest as 'GET' to '/get-stuff'
     * @example NewRestRequest as 'GET' to 'http://localhost:9000/get-stuff'
     * @section Rest Builder
     */
    @Step("RestRequest perform '(DELETE|GET|HEAD|OPTIONS|PATCH|POST|PUT)' on '([^']+)'")
    public void restRequestPerformMethodOnUrl(final String method, final String url) throws IOException {

        // TODO - Support path parameter substitution, another method would be best.

        final RequestSpecification request = getRequest();

        //
        // Create a URL comprising of the Base URL and the value passed in.
        //
        final String fullUrl;
        if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("file://")) {
            fullUrl = url;
        } else {
            fullUrl = RestDriverSubstepsConfiguration.PROPERTIES.getBaseUrl() + url;
        }

        LOG.debug("Creating new fluent Request with Method [{}], URL [{}].", method, fullUrl);

        //
        // Load in all the CookieStores for each Scope available.
        //
        request.cookies(getCookieStores());

        //
        // Add the Request Body data to the request.
        //
        final List<RequestBodyEntry> requestBody = getRequestBodyData();
        if (!requestBody.isEmpty()) {

            // This will explode if the 'NewRequestBody using <RequestBodyBuilder>' step hasn't been run.
            final RequestBodyBuilder bodyBuilder = getRequestBodyBuilder();
            bodyBuilder.build(request, requestBody);
        }

        //
        // Create new Request
        //
        final Response response;
        switch (method.toUpperCase()) {
            case HttpDelete.METHOD_NAME: {
                response = request.delete(fullUrl);
                break;
            }
            case HttpGet.METHOD_NAME: {
                response = request.get(fullUrl);
                break;
            }
            case HttpHead.METHOD_NAME: {
                response = request.head(fullUrl);
                break;
            }
            case HttpOptions.METHOD_NAME: {
                response = request.options(fullUrl);
                break;
            }
            case HttpPatch.METHOD_NAME: {
                response = request.patch(fullUrl);
                break;
            }
            case HttpPost.METHOD_NAME: {
                response = request.post(fullUrl);
                break;
            }
            case HttpPut.METHOD_NAME: {
                response = request.put(fullUrl);
                break;
            }
            default: {
                throw new AssertionError("Supplied HTTP method [" + method + "] is not accepted.");
            }
        }

        //
        // Set the Request on the Scenario scope.
        //
        setResponse(response.then());
    }


    //
    // Headers
    //

    /**
     * Add a header to the current rest request being built with the given name and value
     *
     * @param name  the name of the header to add
     * @param value the value to set in the header
     * @example RestRequest add header with name 'api_key' and value '1234567890'
     * @section Rest Builder
     */
    @Step("RestRequest add header with name '([^']+)' and value '([^']+)'")
    public void restRequestAddHeadWithNameAndValue(final String name, final String value) {

        LOG.debug("Adding to Request header [{}] with value [{}].", name, value);

        getRequest().header(name, value);
    }


    //
    // Cookies
    //

    /**
     * Add a cookie to the current rest request being built with the given name and value
     *
     * @param name  the name of the cookie to add
     * @param value the value to set in the cookie
     * @example RestRequest add cookie with name 'JESSIONID' and value 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
     * @section Rest Builder
     */
    @Step("RestRequest add cookie with name '([^']+)' and value '([^']+)'")
    public void restRequestAddCookieWithNameAndValue(final String name, final String value) {
        restRequestAddCookieWithNameAndValueInScope(name, value, Scope.SCENARIO);
    }

    /**
     * Add a cookie to the given scope with the given name and value, and will be used in any request until the scope expires.
     *
     * @param name  the name of the cookie to add
     * @param value the value to set in the cookie
     * @param scope the scope where the cookie will persist in
     * @example RestRequest add cookie with name 'JESSIONID' and value 'ABCDEFGHIJKLMNOPQRSTUVWXYZ' in scope 'FEATURE'
     * @section Rest Builder
     */
    @Step("RestRequest add cookie with name '([^']+)' and value '([^']+)' in scope '(SUITE|FEATURE|SCENARIO|SCENARIO_BACKGROUND|SCENARIO_OUTLINE|SCENARIO_OUTLINE_ROW|STEP)'")
    public void restRequestAddCookieWithNameAndValueInScope(
            final String name, final String value, @StepParameter(converter = ScopeConverter.class) final Scope scope
    ) {

        LOG.debug("Adding to Request cookie [{}] with value [{}] in scope [{}].", name, value, scope);

        addCookie(name, value, scope);
    }


    //
    // Request Body
    //

    /**
     * Add data to the current request being built, with the name and value provided.
     *
     * @param name  the key name of the data to add
     * @param value the data value to set
     * @example RestRequest add data with name 'name' and value 'my_name'
     * @section Rest Builder
     */
    @Step("RestRequest add data with name '([^']+)' and value '([^']+)'")
    public void restRequestAddDataWithNameAndValue(final String name, final String value) {

        LOG.debug("Adding Data for Rest Request with name [{}] and value [{}]", name, value);

        addToRequestBodyData(name, value);
    }

    /**
     * Add data to the current request being build, at a position with name and value provided.
     *
     * @param position the position in the json array to place the key pair
     * @param name the key of the paired data
     * @param value the value of the paired data
     * @example RestRequest add data at position '0' with name 'username' and value 'bob'
     * @section Rest Builder
     */
    @Step("RestRequest add data at position '([0-9]+)' with name '([^']+)' and value '([^']+)'")
    public void restRequestAddDataAtPositionWithNameAndValue(final int position, final String name, final String value) {

        LOG.debug("Adding Data for Rest Request at position [{}] with name [{}] and value [{}]", position, name, value);

        addToRequestBodyData(new GroupedKeyPairRequestBodyEntry(position, name, value));
    }

    /**
     * Select the type of rest request body builder to be used in the current scenario. Currently there is only support
     * for JsonObjectRequestBodyBuilder (key pairs in json format) and FormRequestBodyBuilder (form submission format).
     *
     * @param builder the type of rest request body builder
     * @example NewRestRequestBody using the 'JsonObjectRequestBodyBuilder'
     * @section Rest Builder
     */
    @Step("RestRequest build body using the '(JsonArrayRequestBodyBuilder|JsonObjectRequestBodyBuilder|FormRequestBodyBuilder)'")
    public void restRequestBuildBodyUsingThe(@StepParameter(converter = RequestBodyBuilderConverter.class) final RequestBodyBuilder builder) {

        LOG.debug("Setting RequestBodyBuilder to [{}]", builder.getClass().getName());

        setRequestBodyBuilder(builder);
    }

    //
    // Request Configuration
    //

    /**
     * Set the user agent string for the current rest request.
     *
     * @param userAgent the string to use as the user agent identifier
     * @example RestRequest set user-agent string as 'SubstepsRestDriver/0.0.1 (+https://github.com/beercan1989/substeps-restdriver)'
     * @section Rest Builder
     */
    @Step("RestRequest set user-agent string as '([^']+)'")
    public void restRequestSetUserAgentStringAs(final String userAgent) {

        LOG.debug("Setting User Agent on Rest Request as [{}].", userAgent);

        getRequest().header("User-Agent", RestDriverSubstepsConfiguration.PROPERTIES.getUserAgent());
    }

    /**
     * Set the proxy for the current rest request.
     *
     * @param proxy the proxy string to use.
     * @example RestRequest set proxy as 'http://localhost:616'
     * @section Rest Builder
     */
    @Step("RestRequest set proxy as '([^']+)'")
    public void restRequestSetProxyAs(final String proxy) {

        LOG.debug("Setting Proxy on Rest Request as [{}]", proxy);

        getRequest().proxy(proxy);
    }

    /**
     * Set the connect timeout for the current rest request.
     *
     * @param timeout the connect timeout in milliseconds
     * @example RestRequest set connect timeout as '5000'
     * @section Rest Builder
     */
//    @Step("RestRequest set connect timeout as '([0-9]+)'")
//    public void restRequestSetConnectTimeoutAs(final int timeout) {
//
//        LOG.debug("Setting Connection Timeout on Rest Request as [{}]", timeout);
//
//        // TODO - Work out how
//        getRequest().connectTimeout(timeout);
//    }

    /**
     * Set the socket timeout for the current rest request.
     *
     * @param timeout the socket timeout in milliseconds
     * @example RestRequest set connect timeout as '5000'
     * @section Rest Builder
     */
//    @Step("RestRequest set socket timeout as '([0-9]+)'")
//    public void restRequestSetSocketTimeoutAs(final int timeout) {
//
//        LOG.debug("Setting Socket Timeout on Rest Request as [{}]", timeout);
//
//        // TODO - Work out how
//        getRequest().socketTimeout(timeout);
//    }

    //
    // Params
    //

    /**
     * Add a parameter to the url for the current rest request being built with the given name and value
     *
     * @param name  the name of the url parameter to add
     * @param value the value to set in the url parameter
     * @example RestRequest add param with name 'name' and value 'bob'
     * @section Rest Builder
     */
    @Step("RestRequest add param with name '([^']+)' and value '([^']+)'")
    public void restRequestAddParamWithNameAndValue(final String name, final String value) {

        LOG.debug("Adding to Request param [{}] with value [{}].", name, value);

        getRequest().param(name, value);
    }
}
