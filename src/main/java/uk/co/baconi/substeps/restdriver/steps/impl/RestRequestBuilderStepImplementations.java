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

import com.technophobia.substeps.model.Scope;
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepImplementations;
import com.technophobia.substeps.model.SubSteps.StepParameter;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.baconi.substeps.restdriver.RestDriverSetupAndTearDown;
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
     * Create a new rest request using the given HTTP method and URL. The URL can either be absolute or relative to the
     * base url in the properties.
     *
     * @param method the HTTP method type to create the request as.
     * @param url    the URL where the request will be sent to.
     * @example NewRestRequest as 'GET' to '/get-stuff'
     * @example NewRestRequest as 'GET' to 'http://localhost:9000/get-stuff'
     * @section Rest Builder
     */
    @Step("NewRestRequest as '(DELETE|GET|HEAD|OPTIONS|PATCH|POST|PUT|TRACE)' to '([^']+)'")
    public void newRequestAsMethodToUrl(final String method, final String url) {

        //
        // Create a URL comprising of the Base URL and the value passed in.
        //
        final String fullURL;
        if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("file://")) {
            fullURL = url;
        } else {
            fullURL = RestDriverSubstepsConfiguration.PROPERTIES.getBaseUrl() + url;
        }

        LOG.debug("Creating new fluent Request with Method [{}], URL [{}].", method, fullURL);

        //
        // Create new Request
        //
        final Request newRequest;
        switch (method.toUpperCase()) {
            case HttpDelete.METHOD_NAME: {
                newRequest = Request.Delete(fullURL);
                break;
            }
            case HttpGet.METHOD_NAME: {
                newRequest = Request.Get(fullURL);
                break;
            }
            case HttpHead.METHOD_NAME: {
                newRequest = Request.Head(fullURL);
                break;
            }
            case HttpOptions.METHOD_NAME: {
                newRequest = Request.Options(fullURL);
                break;
            }
            case HttpPatch.METHOD_NAME: {
                newRequest = Request.Patch(fullURL);
                break;
            }
            case HttpPost.METHOD_NAME: {
                newRequest = Request.Post(fullURL);
                break;
            }
            case HttpPut.METHOD_NAME: {
                newRequest = Request.Put(fullURL);
                break;
            }
            case HttpTrace.METHOD_NAME: {
                newRequest = Request.Trace(fullURL);
                break;
            }
            default: {
                throw new AssertionError("Supplied HTTP method [" + method + "] is not accepted.");
            }
        }

        //
        // Setup default values from properties.
        //
        newRequest.connectTimeout(RestDriverSubstepsConfiguration.PROPERTIES.getConnectTimeout());
        newRequest.socketTimeout(RestDriverSubstepsConfiguration.PROPERTIES.getSocketTimeout());
        newRequest.userAgent(RestDriverSubstepsConfiguration.PROPERTIES.getUserAgent());
        RestDriverSubstepsConfiguration.PROPERTIES.getProxy().ifPresent(newRequest::viaProxy);

        //
        // Set the Request on the Scenario scope.
        //
        setRequest(newRequest);
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

        getRequest().addHeader(name, value);
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

        addToRequestBody(name, value);
    }

    /**
     * Select the type of rest request body builder to be used in the current scenario. Currently there is only support
     * for SimpleJsonRequestBodyBuilder (key pairs in json format) and KeyPairRequestBodyBuilder (form submission format).
     *
     * @param builder the type of rest request body builder
     * @example NewRestRequestBody using the 'SimpleJsonRequestBodyBuilder'
     * @section Rest Builder
     */
    @Step("NewRestRequestBody using the '(SimpleJsonRequestBodyBuilder|KeyPairRequestBodyBuilder)'")
    public void newRequestBodyUsingThe(@StepParameter(converter = RequestBodyBuilderConverter.class) final RequestBodyBuilder builder) {

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

        getRequest().userAgent(userAgent);
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

        getRequest().viaProxy(proxy);
    }

    /**
     * Set the connect timeout for the current rest request.
     *
     * @param timeout the connect timeout in milliseconds
     * @example RestRequest set connect timeout as '5000'
     * @section Rest Builder
     */
    @Step("RestRequest set connect timeout as '([0-9]+)'")
    public void restRequestSetConnectTimeoutAs(final int timeout) {

        LOG.debug("Setting Connection Timeout on Rest Request as [{}]", timeout);

        getRequest().connectTimeout(timeout);
    }

    /**
     * Set the socket timeout for the current rest request.
     *
     * @param timeout the socket timeout in milliseconds
     * @example RestRequest set connect timeout as '5000'
     * @section Rest Builder
     */
    @Step("RestRequest set socket timeout as '([0-9]+)'")
    public void restRequestSetSocketTimeoutAs(final int timeout) {

        LOG.debug("Setting Socket Timeout on Rest Request as [{}]", timeout);

        getRequest().socketTimeout(timeout);
    }


    //
    // Executing Request
    //

    /**
     * Execute the current rest request, using config from previous steps and cookies from all available scopes.
     *
     * @throws IOException if there are problems executing the http request
     * @example ExecuteRestRequest with available configuration
     * @section Rest Builder
     */
    @Step("ExecuteRestRequest with available configuration")
    public void executeRestRequestWithAvailableConfiguration() throws IOException {

        LOG.debug("Executing current Rest Request, with available cookies.");

        final Executor executor = Executor.newInstance();

        // Load in all the CookieStores for each Scope available.
        executor.use(getCookieStores());

        final Request request = getRequest();

        // Add the Request Body data to the request.
        final List<RequestBodyEntry> requestBody = getRequestBody();
        if (!requestBody.isEmpty()) {

            // This will explode if the 'NewRequestBody using <RequestBodyBuilder>' step hasn't been run.
            final RequestBodyBuilder bodyBuilder = getRequestBodyBuilder();
            bodyBuilder.build(request, requestBody);
        }

        // Attempt to execute Request and Save response or throw the error
        final Response response = executor.execute(request);
        setResponse(response);
    }
}
