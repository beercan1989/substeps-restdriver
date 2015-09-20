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

package co.uk.baconi.substeps.restdriver.steps;

import co.uk.baconi.substeps.restdriver.builders.RequestBodyBuilder;
import co.uk.baconi.substeps.restdriver.builders.RequestBodyEntry;
import co.uk.baconi.substeps.restdriver.utils.IOUtil;
import com.technophobia.substeps.model.Scope;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static co.uk.baconi.substeps.restdriver.utils.ExecutionContextUtil.*;

public abstract class AbstractRestDriverSubStepImplementations {

    private static final String BASE_REST_DRIVER = "REST_DRIVER_";
    private static final String CURRENT_REST_REQUEST = BASE_REST_DRIVER + "CURRENT_REST_REQUEST";
    private static final String CURRENT_REST_REQUEST_BODY = BASE_REST_DRIVER + "CURRENT_REST_REQUEST_BODY";
    private static final String CURRENT_REST_REQUEST_BUILDER = BASE_REST_DRIVER + "CURRENT_REST_REQUEST_BUILDER";
    private static final String CURRENT_REST_RESPONSE = BASE_REST_DRIVER + "CURRENT_REST_RESPONSE";
    private static final String CURRENT_COOKIE_STORE = BASE_REST_DRIVER + "CURRENT_COOKIE_STORE";
    private static final String CURRENT_JSON_ELEMENT = BASE_REST_DRIVER + "CURRENT_JSON_ELEMENT";

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    //
    // Request
    //
    protected void setRequest(final Request request) {
        setRequest(request, Scope.SCENARIO);
    }

    protected void setRequest(final Request request, final Scope scope) {
        put(scope, CURRENT_REST_REQUEST, request);
    }

    protected Request getRequest() {
        return getRequest(Scope.SCENARIO);
    }

    protected Request getRequest(final Scope scope) {
        return getOrThrowError("RestRequest", () -> get(scope, CURRENT_REST_REQUEST, Request.class));
    }


    //
    // Request Body
    //
    protected void addToRequestBody(final String key, final String value) {
        addToRequestBody(new RequestBodyEntry(key, value));
    }

    protected void addToRequestBody(final RequestBodyEntry entry) {
        final List<RequestBodyEntry> requestBody = getRequestBody();
        requestBody.add(entry);
        setRequestBody(requestBody);
    }

    protected void setRequestBody(final List<RequestBodyEntry> body) {
        setRequestBody(Scope.SCENARIO, body);
    }

    protected void setRequestBody(final Scope scope, final List<RequestBodyEntry> body) {
        put(scope, CURRENT_REST_REQUEST_BODY, body);
    }

    protected List<RequestBodyEntry> getRequestBody() {
        return getRequestBody(Scope.SCENARIO);
    }

    protected List<RequestBodyEntry> getRequestBody(final Scope scope) {
        return getList(scope, CURRENT_REST_REQUEST_BODY);
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
    protected void setResponse(final Response response) throws IOException {
        setResponse(response, Scope.SCENARIO);
    }

    protected void setResponse(final Response response, final Scope scope) throws IOException {
        put(scope, CURRENT_REST_RESPONSE, response.returnResponse());
    }

    protected HttpResponse getResponse() {
        return getResponse(Scope.SCENARIO);
    }

    protected HttpResponse getResponse(final Scope scope) {
        return getOrThrowError("RestResponse", () -> get(scope, CURRENT_REST_RESPONSE, HttpResponse.class));
    }


    //
    // Response Body
    //
    protected String getResponseBody() throws IOException {
        return getResponseBody(Scope.SCENARIO);
    }

    protected String getResponseBody(final Scope scope) throws IOException {
        return IOUtil.readAll(getRawResponseBody(scope));
    }

    protected InputStream getRawResponseBody() throws IOException {
        return getRawResponseBody(Scope.SCENARIO);
    }

    protected InputStream getRawResponseBody(final Scope scope) throws IOException {
        return getResponse(scope).getEntity().getContent();
    }


    //
    // Cookies
    //
    protected void addCookie(final String name, final String value) {
        addCookie(name, value, Scope.SCENARIO);
    }

    protected void addCookie(final Cookie cookie) {
        addCookie(cookie, Scope.SCENARIO);
    }

    protected void addCookie(final String name, final String value, final Scope scope) {
        addCookie(new BasicClientCookie(name, value), scope);
    }

    protected void addCookie(final Cookie cookie, final Scope scope) {
        final BasicCookieStore cookieStore = getCookieStore(scope).orElseGet(BasicCookieStore::new);
        cookieStore.addCookie(cookie);
        setCookieStore(cookieStore, scope);
    }

    protected void setCookieStore(final BasicCookieStore cookieStore) {
        setCookieStore(cookieStore, Scope.SCENARIO);
    }

    protected void setCookieStore(final BasicCookieStore cookieStore, final Scope scope) {
        put(scope, CURRENT_COOKIE_STORE, cookieStore);
    }

    protected BasicCookieStore getCookieStores() {
        return EnumSet.
                allOf(Scope.class).
                stream().
                map(
                        this::getCookieStore
                ).
                map(
                        store -> store.orElseGet(BasicCookieStore::new)
                ).
                reduce(new BasicCookieStore(),
                        (result, entry) -> {
                            result.getCookies().addAll(entry.getCookies());
                            return result;
                        }
                );
    }

    protected Optional<BasicCookieStore> getCookieStore(final Scope scope) {
        return get(scope, CURRENT_COOKIE_STORE, BasicCookieStore.class);
    }


    //
    // JSON
    //
    protected <A> void setCurrentJsonElement(final A element) {
        setCurrentJsonElement(element, Scope.SCENARIO);
    }

    protected <A> void setCurrentJsonElement(final A element, final Scope scope) {
        put(scope, CURRENT_JSON_ELEMENT, element);
    }

    protected <A> A getCurrentJsonElement(final Class<A> expectedType) {
        return getCurrentJsonElement(expectedType, Scope.SCENARIO);
    }

    protected <A> A getCurrentJsonElement(final Class<A> expectedType, final Scope scope) {
        return getOrThrowError("CurrentJsonElement", () -> get(scope, CURRENT_JSON_ELEMENT, expectedType));
    }


    //
    // Helpers
    //
    protected <A> A getOrThrowError(final String getting, Supplier<Optional<A>> getter) {
        return getter.get().orElseThrow(
                () -> new AssertionError("Unable to find the [" + getting + "].")
        );
    }
}
