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

package uk.co.baconi.substeps.restdriver.builders.impl;

import com.google.common.io.Resources;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import uk.co.baconi.substeps.restdriver.builders.FromUrlRequestBodyEntry;
import uk.co.baconi.substeps.restdriver.builders.RequestBodyBuilder;
import uk.co.baconi.substeps.restdriver.builders.RequestBodyEntry;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

public class JsonFromUriRequestBodyBuilder extends RequestBodyBuilder {

    @Override
    public void build(final RequestSpecification request, final List<RequestBodyEntry> data) {

        final FromUrlRequestBodyEntry fromUrl = verifyDataIs(data, FromUrlRequestBodyEntry.class)
                .findFirst()
                .orElseThrow(
                        () -> new AssertionError("Missing RequestBodyEntry of type FromUrlRequestBodyEntry.")
                );

        try {

            final String body = Resources.toString(fromUrl.getUrl(), Charset.forName("UTF-8"));

            request.contentType(ContentType.JSON).body(body);
        } catch (final IOException exception) {

            throw new AssertionError("Unable to read source [" + fromUrl.getUrl().toString() + "]", exception);
        }
    }

}
