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
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepImplementations;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@StepImplementations(requiredInitialisationClasses = RestDriverSetupAndTearDown.class)
public class RestJsonAssertionStepImplementations extends AbstractRestDriverSubStepImplementations {

    private final RestJsonFinderStepImplementations finderImpl;

    public RestJsonAssertionStepImplementations() {
        finderImpl = new RestJsonFinderStepImplementations();
    }

    //
    // AssertRestResponseBody
    //

    @Step("AssertRestResponseBody is JSON '(object|array)'")
    public void assertRestResponseBodyIsJson(final String type) throws IOException {

        logger.debug("Asserting that the body is a JSON [" + type + "]");

        switch (type) {
            case "object": {
                finderImpl.findJsonElementByJsonPathInRestResponseBodyAnObject(".");
                break;
            }
            case "array": {
                finderImpl.findJsonElementByJsonPathInRestResponseBodyAnArray(".");
                break;
            }
            default: {
                throw new AssertionError("Unsupported JSON type to check [" + type + "].");
            }
        }
    }

    //
    // AssertJsonElement ByJsonPath
    //

    @Step("AssertJsonElement ByJsonPath '([^']+)' in RestResponseBody a 'string' with value: (.*)")
    public void assertJsonElementByJsonPathInRestResponseBodyAStringWithValue(final String jsonPath,final String expectedValue) throws IOException {
        logger.debug("Asserting that by JsonPath [" + jsonPath + "] there is a string with value: " + expectedValue);

        final String result = finderImpl.findJsonElementByJsonPathInRestResponseBodyAString(jsonPath);

        assertThat(result, is(equalToIgnoringWhiteSpace(expectedValue)));
    }

    @Step("AssertJsonElement ByJsonPath '([^']+)' in RestResponseBody a 'number' with value: ([0-9]+(?:\\.[0-9]+)?)")
    public void assertJsonElementByJsonPathInRestResponseBodyAStringWithValue(final String jsonPath, final double expectedValue) throws IOException {
        logger.debug("Asserting that by JsonPath [" + jsonPath + "] there is a string with value: " + expectedValue);

        final Number result = finderImpl.findJsonElementByJsonPathInRestResponseBodyANumber(jsonPath);

        assertThat(result, is(equalTo(expectedValue)));
    }

    @Step("AssertJsonElement ByJsonPath '([^']+)' in RestResponseBody (a|an) '(object|array)'")
    public void assertJsonElementByJsonPathInRequestResponseBody(final String jsonPath, final String aOrAn, final String type) throws IOException {
        logger.debug("Asserting that by JsonPath [" + jsonPath + "] there is " + aOrAn + " [" + type + "]");

        switch (type) {
            case "object": {
                finderImpl.findJsonElementByJsonPathInRestResponseBodyAnObject(jsonPath);
                break;
            }
            case "array": {
                finderImpl.findJsonElementByJsonPathInRestResponseBodyAnArray(jsonPath);
                break;
            }
            default: {
                throw new AssertionError("Unsupported JSON type to check [" + type + "].");
            }
        }
    }

    //
    // AssertJsonElement ByPreviousFind
    //

    @Step("AssertJsonElement ByPreviousFind in RestResponseBody is a 'string' with value: (.*)")
    public void assertCurrentJsonElementByPreviousFindInRestResponseBodyIsAStringWithValue(final String expectedValue) {
        logger.debug("Asserting that JsonElement from RestResponseBody is a string with value: " + expectedValue);

        final String currentJsonElement = getCurrentJsonElement(String.class);

        assertThat(currentJsonElement, is(equalTo(expectedValue)));
    }

    @Step("AssertJsonElement ByPreviousFind in RestResponseBody is a 'number' with value: ([0-9]+(?:\\.[0-9]+)?)")
    public void assertCurrentJsonElementByPreviousFindInRestResponseBodyIsANumberWithValue(final double expectedValue) {
        logger.debug("Asserting that JsonElement from RestResponseBody is a string with value: " + expectedValue);

        final Number currentJsonElement = getCurrentJsonElement(Number.class);

        assertThat(currentJsonElement, is(equalTo(expectedValue)));
    }
}
