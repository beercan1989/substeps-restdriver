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
import uk.co.baconi.substeps.restdriver.builders.GroupedKeyPairRequestBodyEntry;
import uk.co.baconi.substeps.restdriver.builders.KeyPairRequestBodyEntry;
import uk.co.baconi.substeps.restdriver.builders.RequestBodyBuilder;
import uk.co.baconi.substeps.restdriver.builders.RequestBodyEntry;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

public class JsonArrayRequestBodyBuilder extends RequestBodyBuilder {

    @Override
    public void build(final RequestSpecification request, final List<RequestBodyEntry> data) {

        final Map<Integer, List<GroupedKeyPairRequestBodyEntry>> groupedData = verifyDataIs(
                data, GroupedKeyPairRequestBodyEntry.class
        ).collect(
                groupingBy(GroupedKeyPairRequestBodyEntry::getGroup)
        );

        final List<Map<String, String>> jsonBody = groupedData.values().stream().map(entry -> entry
                .stream()
                .collect(
                        toMap(KeyPairRequestBodyEntry::getKey, KeyPairRequestBodyEntry::getValue)
                )
        ).collect(
                toList()
        );

        request.contentType(ContentType.JSON).body(jsonBody);
    }

}
