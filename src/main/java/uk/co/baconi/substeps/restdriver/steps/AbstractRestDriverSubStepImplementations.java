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

package uk.co.baconi.substeps.restdriver.steps;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ValidatableResponse;
import com.jayway.restassured.specification.RequestSpecification;
import com.technophobia.substeps.model.Scope;
import uk.co.baconi.substeps.restdriver.builders.RequestBodyBuilder;
import uk.co.baconi.substeps.restdriver.builders.RequestBodyEntry;
import uk.co.baconi.substeps.restdriver.properties.RestDriverSubstepsConfiguration;

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

import static uk.co.baconi.substeps.restdriver.utils.ExecutionContextUtil.*;

public abstract class AbstractRestDriverSubStepImplementations {

    private static final String BASE_REST_DRIVER = "REST_DRIVER_";
    private static final String REQUEST = BASE_REST_DRIVER + "REQUEST";
    private static final String REQUEST_BODY_DATA = BASE_REST_DRIVER + "REQUEST_BODY_DATA";
    private static final String CURRENT_REST_REQUEST_BUILDER = BASE_REST_DRIVER + "REQUEST_BODY_BUILDER";
    private static final String CURRENT_REST_RESPONSE = BASE_REST_DRIVER + "RESPONSE";
    private static final String CURRENT_COOKIE_STORE = BASE_REST_DRIVER + "COOKIES";
    //private static final String CURRENT_JSON_ELEMENT = BASE_REST_DRIVER + "CURRENT_JSON_ELEMENT";

    //
    // Request
    //
    protected void setRequest(final RequestSpecification request) {
        setRequest(request, Scope.SCENARIO);
    }

    protected void setRequest(final RequestSpecification request, final Scope scope) {
        put(scope, REQUEST, request);
    }

    protected RequestSpecification getRequest() {
        return getRequest(Scope.SCENARIO);
    }

    protected RequestSpecification getRequest(final Scope scope) {
        return get(scope, REQUEST, RequestSpecification.class).orElseGet(this::createNewRequest);
    }

    protected RequestSpecification createNewRequest() {

        RequestSpecification request = RestAssured.given();

        //
        // Setup default values from properties.
        //
        //response.connectTimeout(RestDriverSubstepsConfiguration.PROPERTIES.getConnectTimeout());
        //response.socketTimeout(RestDriverSubstepsConfiguration.PROPERTIES.getSocketTimeout());
        request.header("User-Agent", RestDriverSubstepsConfiguration.PROPERTIES.getUserAgent());
        RestDriverSubstepsConfiguration.PROPERTIES.getProxy().ifPresent(request::proxy);

        return request;
    }


    //
    // Request Body
    //
    protected void addToRequestBodyData(final String key, final String value) {
        addToRequestBodyData(new RequestBodyEntry(key, value));
    }

    protected void addToRequestBodyData(final RequestBodyEntry entry) {
        final List<RequestBodyEntry> requestBody = getRequestBodyData();
        requestBody.add(entry);
        setRequestBodyData(requestBody);
    }

    protected void setRequestBodyData(final List<RequestBodyEntry> body) {
        setRequestBodyData(Scope.SCENARIO, body);
    }

    protected void setRequestBodyData(final Scope scope, final List<RequestBodyEntry> body) {
        put(scope, REQUEST_BODY_DATA, body);
    }

    protected List<RequestBodyEntry> getRequestBodyData() {
        return getRequestBodyData(Scope.SCENARIO);
    }

    protected List<RequestBodyEntry> getRequestBodyData(final Scope scope) {
        return getList(scope, REQUEST_BODY_DATA);
    }


    //
    // Request Builder
    //
    protected void setRequestBodyBuilder(final RequestBodyBuilder builder) {
        setRequestBodyBuilder(builder, Scope.SCENARIO);
    }

    protected void setRequestBodyBuilder(final RequestBodyBuilder builder, final Scope scope) {
        put(scope, CURRENT_REST_REQUEST_BUILDER, builder);
    }

    protected RequestBodyBuilder getRequestBodyBuilder() {
        return getRequestBodyBuilder(Scope.SCENARIO);
    }

    protected RequestBodyBuilder getRequestBodyBuilder(final Scope scope) {
        return getOrThrowError("RestRequestBodyBuilder", () -> get(scope, CURRENT_REST_REQUEST_BUILDER, RequestBodyBuilder.class));
    }


    //
    // Response
    //
    protected void setResponse(final ValidatableResponse response) throws IOException {
        setResponse(response, Scope.SCENARIO);
    }

    protected void setResponse(final ValidatableResponse response, final Scope scope) throws IOException {
        put(scope, CURRENT_REST_RESPONSE, response);
    }

    protected ValidatableResponse getResponse() {
        return getResponse(Scope.SCENARIO);
    }

    protected ValidatableResponse getResponse(final Scope scope) {
        return getOrThrowError("RestResponse", () -> get(scope, CURRENT_REST_RESPONSE, ValidatableResponse.class));
    }


    //
    // Response Body
    //
//    protected String getResponseBody() throws IOException {
//        return getResponseBody(Scope.SCENARIO);
//    }

//    protected String getResponseBody(final Scope scope) throws IOException {
//        return IOUtil.readAll(getRawResponseBody(scope));
//    }

//    protected InputStream getRawResponseBody() throws IOException {
//        return getRawResponseBody(Scope.SCENARIO);
//    }

//    protected InputStream getRawResponseBody(final Scope scope) throws IOException {
//        return getResponse(scope).getEntity().getContent();
//    }


    //
    // Cookies
    //
    protected void addCookie(final String name, final String value, final Scope scope) {
        final Map<String, String> cookieStore = getCookieStore(scope);
        cookieStore.put(name, value);
        setCookieStore(cookieStore, scope);
    }

    protected void setCookieStore(final Map<String, String> cookieStore) {
        setCookieStore(cookieStore, Scope.SCENARIO);
    }

    protected void setCookieStore(final Map<String, String> cookieStore, final Scope scope) {
        put(scope, CURRENT_COOKIE_STORE, cookieStore);
    }

    protected Map<String, String> getCookieStores() {
        return EnumSet.
                allOf(Scope.class).
                stream().
                map(
                        this::getCookieStore
                ).
                reduce(new HashMap<String, String>(),
                        (result, entry) -> {
                            result.putAll(entry);
                            return result;
                        }
                );
    }

    protected Map<String, String> getCookieStore(final Scope scope) {
        return getMap(scope, CURRENT_COOKIE_STORE);
    }


    //
    // JSON
    //
//    protected <A> void setCurrentJsonElement(final A element) {
//        setCurrentJsonElement(element, Scope.SCENARIO);
//    }
//
//    protected <A> void setCurrentJsonElement(final A element, final Scope scope) {
//        put(scope, CURRENT_JSON_ELEMENT, element);
//    }
//
//    protected <A> A getCurrentJsonElement(final Class<A> expectedType) {
//        return getCurrentJsonElement(expectedType, Scope.SCENARIO);
//    }
//
//    protected <A> A getCurrentJsonElement(final Class<A> expectedType, final Scope scope) {
//        return getOrThrowError("CurrentJsonElement", () -> get(scope, CURRENT_JSON_ELEMENT, expectedType));
//    }


    //
    // Helpers
    //
    protected <A> A getOrThrowError(final String getting, final Supplier<Optional<A>> getter) {
        return getter.get().orElseThrow(
                () -> new AssertionError("Unable to find the [" + getting + "].")
        );
    }
}
