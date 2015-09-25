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

package co.uk.baconi.substeps.restdriver.steps.impl;

import co.uk.baconi.substeps.restdriver.RestDriverSetupAndTearDown;
import co.uk.baconi.substeps.restdriver.builders.RequestBodyBuilder;
import co.uk.baconi.substeps.restdriver.builders.RequestBodyEntry;
import co.uk.baconi.substeps.restdriver.converters.RequestBodyBuilderConverter;
import co.uk.baconi.substeps.restdriver.converters.ScopeConverter;
import co.uk.baconi.substeps.restdriver.properties.RestDriverSubstepsConfiguration;
import co.uk.baconi.substeps.restdriver.steps.AbstractRestDriverSubStepImplementations;
import com.technophobia.substeps.model.Scope;
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepImplementations;
import com.technophobia.substeps.model.SubSteps.StepParameter;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.*;

import java.io.IOException;
import java.util.List;

@StepImplementations(requiredInitialisationClasses = RestDriverSetupAndTearDown.class)
public class RestRequestBuilderStepImplementations extends AbstractRestDriverSubStepImplementations {

    @Step("NewRestRequest as '(DELETE|GET|HEAD|OPTIONS|PATCH|POST|PUT|TRACE)' to '([^']+)'")
    public void newRequestAsMethodToUrl(final String method, final String url) {

        //
        // Create a URL comprising of the Base URL and the value passed in.
        //
        final String fullURL;
        if(url.startsWith("http://") || url.startsWith("https://") || url.startsWith("file://")) {
            fullURL = url;
        } else {
            fullURL = RestDriverSubstepsConfiguration.PROPERTIES.getBaseUrl() + url;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Creating new fluent Request with Method [" + method + "], URL [" + fullURL + "].");
        }

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

    @Step("RestRequest add header with name '([^']+)' and value '([^']+)'")
    public void restRequestAddHeadWithNameAndValue(final String name, final String value) {

        logger.debug("Adding to Request header [" + name + "] with value [" + value + "].");

        getRequest().addHeader(name, value);
    }


    //
    // Cookies
    //

    @Step("RestRequest add cookie with name '([^']+)' and value '([^']+)'")
    public void restRequestAddCookieWithNameAndValue(final String name, final String value) {
        restRequestAddCookieWithNameAndValueInScope(name, value, Scope.SCENARIO);
    }

    @Step("RestRequest add cookie with name '([^']+)' and value '([^']+)' in scope '(SUITE|FEATURE|SCENARIO|SCENARIO_BACKGROUND|SCENARIO_OUTLINE|SCENARIO_OUTLINE_ROW|STEP)'")
    public void restRequestAddCookieWithNameAndValueInScope(
            final String name, final String value, @StepParameter(converter = ScopeConverter.class) final Scope scope
    ) {

        logger.debug("Adding to Request cookie [" + name + "] with value [" + value + "] in scope [" + scope + "].");

        addCookie(name, value, scope);
    }


    //
    // Request Body
    //

    @Step("RestRequest add data with name '([^']+)' and value '([^']+)'")
    public void restRequestAddDataWithNameAndValue(final String name, final String value) {

        logger.debug("Adding Data for Rest Request with name [" + name + "] and value [" + value + "]");

        addToRequestBody(name, value);
    }

    @Step("NewRestRequestBody using the '(SimpleJsonRequestBodyBuilder|KeyPairRequestBodyBuilder)'")
    public void newRequestBodyUsingThe(@StepParameter(converter = RequestBodyBuilderConverter.class) final RequestBodyBuilder builder) {

        logger.debug("Setting RequestBodyBuilder to [" + builder.getClass().getName() + "]");

        setRequestBodyBuilder(builder);
    }

    //
    // Request Configuration
    //

    @Step("RestRequest set user-agent string as '([^']+)'")
    public void restRequestSetUserAgentStringAs(final String userAgent) {

        logger.debug("Setting User Agent on Rest Request as [" + userAgent + "].");

        getRequest().userAgent(userAgent);
    }

    @Step("RestRequest set proxy as '([^']+)'")
    public void restRequestSetProxyAs(final String proxy) {

        logger.debug("Setting Proxy on Rest Request as [" + proxy + "]");

        getRequest().viaProxy(proxy);
    }

    @Step("RestRequest set connect timeout as '([0-9]+)'")
    public void restRequestSetConnectTimeoutAs(final int timeout) {

        logger.debug("Setting Connection Timeout on Rest Request as [" + timeout + "]");

        getRequest().connectTimeout(timeout);
    }

    @Step("RestRequest set socket timeout as '([0-9]+)'")
    public void restRequestSetSocketTimeoutAs(final int timeout) {

        logger.debug("Setting Socket Timeout on Rest Request as [" + timeout + "]");

        getRequest().socketTimeout(timeout);
    }


    //
    // Executing Request
    //

    @Step("ExecuteRestRequest with available configuration")
    public void executeRestRequestWithAvailableConfiguration() throws IOException {

        logger.debug("Executing current Rest Request, with available cookies.");

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
