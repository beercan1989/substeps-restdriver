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

package co.uk.baconi.substeps.restdriver.steps.impl.json;

import co.uk.baconi.substeps.restdriver.RestDriverSetupAndTearDown;
import co.uk.baconi.substeps.restdriver.steps.AbstractRestDriverSubStepImplementations;
import com.jayway.jsonpath.JsonPath;
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepImplementations;
import org.hamcrest.Matcher;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;

@StepImplementations(requiredInitialisationClasses = RestDriverSetupAndTearDown.class)
class RestJsonFinderStepImplementations extends AbstractRestDriverSubStepImplementations {

    @Step("FindJsonElement ByJsonPath '([^']+)' in RestResponseBody an 'object'")
    public Map<String, Object> findJsonElementByJsonPathInRestResponseBodyAnObject(final String jsonPath) throws IOException {
        return findJsonElementByJsonPathInRestResponseBodyImpl(jsonPath, isA(Map.class));
    }

    @Step("FindJsonElement ByJsonPath '([^']+)' in RestResponseBody an 'array'")
    public List<Object> findJsonElementByJsonPathInRestResponseBodyAnArray(final String jsonPath) throws IOException {
        return findJsonElementByJsonPathInRestResponseBodyImpl(jsonPath, isA(List.class));
    }

    @Step("FindJsonElement ByJsonPath '([^']+)' in RestResponseBody a 'string'")
    public String findJsonElementByJsonPathInRestResponseBodyAString(final String jsonPath) throws IOException {
        return findJsonElementByJsonPathInRestResponseBodyImpl(jsonPath, isA(String.class));
    }

    @Step("FindJsonElement ByJsonPath '([^']+)' in RestResponseBody a 'number'")
    public Number findJsonElementByJsonPathInRestResponseBodyANumber(final String jsonPath) throws IOException {
        return findJsonElementByJsonPathInRestResponseBodyImpl(jsonPath, isA(Number.class));
    }

    private <A, B extends A> B findJsonElementByJsonPathInRestResponseBodyImpl(final String jsonPath, final Matcher<A> expectedType) throws IOException {

        logger.debug("Find by JsonPath [" + jsonPath + "] and expected type [" + expectedType + "]");

        // Find by path
        final B searchedJson = JsonPath.parse(getRawResponseBody()).read(jsonPath);

        // Assert that something is there
        assertThat(searchedJson, is(expectedType));

        // Store as current read value
        setCurrentJsonElement(searchedJson);

        return searchedJson;
    }

}