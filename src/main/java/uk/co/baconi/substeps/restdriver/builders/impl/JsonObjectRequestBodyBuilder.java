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

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import uk.co.baconi.substeps.restdriver.builders.KeyPairRequestBodyEntry;
import uk.co.baconi.substeps.restdriver.builders.RequestBodyBuilder;
import uk.co.baconi.substeps.restdriver.builders.RequestBodyEntry;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonObjectRequestBodyBuilder extends RequestBodyBuilder {

    @Override
    public void build(final RequestSpecification request, final List<RequestBodyEntry> data) {

        final Map<String, String> body = verifyDataIs(data, KeyPairRequestBodyEntry.class).collect(
                Collectors.toMap(KeyPairRequestBodyEntry::getKey, KeyPairRequestBodyEntry::getValue)
        );

        request.contentType(ContentType.JSON).body(body);
    }

}
