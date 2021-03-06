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

package uk.co.baconi.substeps.restdriver.steps.impl.json;

import com.technophobia.substeps.model.Scope;
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepImplementations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.baconi.substeps.restdriver.RestDriverSetupAndTearDown;
import uk.co.baconi.substeps.restdriver.steps.AbstractRestDriverSubStepImplementations;
import uk.co.baconi.substeps.restdriver.utils.ExecutionContextUtil;

import java.io.IOException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@StepImplementations(requiredInitialisationClasses = RestDriverSetupAndTearDown.class)
public class RestJsonAssertionStepImplementations extends AbstractRestDriverSubStepImplementations {

    private static final Logger LOG = LoggerFactory.getLogger(RestJsonAssertionStepImplementations.class);

    private final RestJsonFinderStepImplementations finderImpl;

    public RestJsonAssertionStepImplementations() {
        finderImpl = new RestJsonFinderStepImplementations();
    }

    /**
     * Assert that the rest response body is the given JSON base type.
     * For JsonPath See: https://github.com/jayway/JsonPath#getting-started
     *
     * @param type the JsonPath to search with.
     * @throws IOException if the response body could not be read
     * @example AssertRestResponseBody is JSON 'object'
     * @section Rest Assertion - JSON
     */
    @Step("AssertRestResponseBody is JSON '(object|array)'")
    public void assertRestResponseBodyIsJson(final String type) throws IOException {

        LOG.debug("Asserting that the body is a JSON [{}]", type);

        switch (type) {
            case "object": {
                finderImpl.findJsonElementByJsonPathInRestResponseBodyAnObject("$");
                break;
            }
            case "array": {
                finderImpl.findJsonElementByJsonPathInRestResponseBodyAnArray("$");
                break;
            }
            default: {
                throw new AssertionError("Unsupported JSON type to check [" + type + "].");
            }
        }
    }

    /**
     * Assert that at the given JsonPath there is a string with the given value.
     * For JsonPath See: https://github.com/jayway/JsonPath#getting-started
     *
     * @param jsonPath      the JsonPath to search with
     * @param expectedValue the expected string to find
     * @throws IOException if the response body could not be read
     * @example AssertJsonElement ByJsonPath '$.someString' in RestResponseBody a 'string' with value: test string
     * @section Rest Assertion - JSON
     */
    @Step("AssertJsonElement ByJsonPath '([^']+)' in RestResponseBody a 'string' with value: (.*)")
    public void assertJsonElementByJsonPathInRestResponseBodyAStringWithValue(final String jsonPath, final String expectedValue) throws IOException {
        LOG.debug("Asserting that by JsonPath [{}] there is a string with value: {}", jsonPath, expectedValue);

        final String result = finderImpl.findJsonElementByJsonPathInRestResponseBodyAString(jsonPath);

        assertThat(result, is(equalToIgnoringWhiteSpace(expectedValue)));
    }

    /**
     * Assert that at the given JsonPath there is a string that contains the given value.
     * For JsonPath See: https://github.com/jayway/JsonPath#getting-started
     *
     * @param jsonPath      the JsonPath to search with
     * @param expectedValue the expected string to find
     * @throws IOException if the response body could not be read
     * @example AssertJsonElement ByJsonPath 'someString' in RestResponseBody a 'string' contains value: test
     * @section Rest Assertion - JSON
     */
    @Step("AssertJsonElement ByJsonPath '([^']+)' in RestResponseBody a 'string' contains value: (.*)")
    public void assertJsonElementByJsonPathInRestResponseBodyAStringContainsValue(final String jsonPath, final String expectedValue) throws IOException {
        LOG.debug("Asserting that by JsonPath [{}] there is a string containing value: {}", jsonPath, expectedValue);

        final String result = finderImpl.findJsonElementByJsonPathInRestResponseBodyAString(jsonPath);

        assertThat(result, containsString(expectedValue));
    }

    /**
     * Assert that at the given JsonPath there is a string that contains the value stored in the scenario variable.
     * For JsonPath See: https://github.com/jayway/JsonPath#getting-started
     *
     * @param jsonPath      the JsonPath to search with
     * @param variableName the scenario variable name
     * @throws IOException if the response body could not be read
     * @example AssertJsonElement ByJsonPath 'someString' in RestResponseBody a 'string' with value in scenario variable: TEST
     * @section Rest Assertion - JSON
     */
    @Step("AssertJsonElement ByJsonPath '([^']+)' in RestResponseBody a 'string' with value in scenario variable: (.*)")
    public void assertJsonElementByJsonPathInRestResponseBodyAStringWithValueInScenarioVariable(final String jsonPath, final String variableName) throws IOException {
        LOG.debug("Asserting that by JsonPath [{}] there is a string matching the value in scenario variable: {}", jsonPath, variableName);

        final String result = finderImpl.findJsonElementByJsonPathInRestResponseBodyAString(jsonPath);

        final String expectedValue = getCustomVariable(variableName, String.class);

        assertThat(result, is(equalTo(expectedValue)));
    }

    /**
     * Assert that at the given JsonPath there is a number with the given value.
     * For JsonPath See: https://github.com/jayway/JsonPath#getting-started
     *
     * @param jsonPath      the JsonPath to search with
     * @param expectedValue the expected number to find
     * @throws IOException if the response body could not be read
     * @example AssertJsonElement ByJsonPath '$.someNumber' in RestResponseBody a 'number' with value: 666
     * @section Rest Assertion - JSON
     */
    @Step("AssertJsonElement ByJsonPath '([^']+)' in RestResponseBody a 'number' with value: ([\\.0-9]+)")
    public void assertJsonElementByJsonPathInRestResponseBodyANumberWithValue(final String jsonPath, final double expectedValue) throws IOException {
        LOG.debug("Asserting that by JsonPath [{}] there is a string with value: {}", jsonPath, expectedValue);

        final double result = finderImpl.findJsonElementByJsonPathInRestResponseBodyANumber(jsonPath).doubleValue();

        assertThat(result, is(equalTo(expectedValue)));
    }

    /**
     * Assert that at the given JsonPath there is a boolean with the given value.
     * For JsonPath See: https://github.com/jayway/JsonPath#getting-started
     *
     * @param jsonPath      the JsonPath to search with
     * @param expectedValue the expected boolean to find
     * @throws IOException if the response body could not be read
     * @example AssertJsonElement ByJsonPath '$.someBoolean' in RestResponseBody a 'boolean' with value: false
     * @section Rest Assertion - JSON
     */
    @Step("AssertJsonElement ByJsonPath '([^']+)' in RestResponseBody a 'boolean' with value: ([tT][rR][uU][eE]|[fF][aA][lL][sS][eE])")
    public void assertJsonElementByJsonPathInRestResponseBodyABooleanWithValue(final String jsonPath, final boolean expectedValue) throws IOException {
        LOG.debug("Asserting that by JsonPath [{}] there is a string with value: {}", jsonPath, expectedValue);

        final boolean result = finderImpl.findJsonElementByJsonPathInRestResponseBodyABoolean(jsonPath);

        assertThat(result, is(equalTo(expectedValue)));
    }

    /**
     * Assert that at the given JsonPath there is the given base JSON type.
     * For JsonPath See: https://github.com/jayway/JsonPath#getting-started
     *
     * @param jsonPath the JsonPath to search with
     * @param aOrAn    a or an, it doesn't matcher which, only useful for reading and logging
     * @param type     the base JSON type to expect
     * @throws IOException if the response body could not be read
     * @example AssertJsonElement ByJsonPath '$.something.someArray' in RestResponseBody an 'array'
     * @section Rest Assertion - JSON
     */
    @Step("AssertJsonElement ByJsonPath '([^']+)' in RestResponseBody (a|an) '(object|array)'")
    public void assertJsonElementByJsonPathInRequestResponseBody(final String jsonPath, final String aOrAn, final String type) throws IOException {
        LOG.debug("Asserting that by JsonPath [{}] there is {} [{}]", jsonPath, aOrAn, type);

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

    /**
     * Assert that at the last find by JsonPath there is a string with the given value.
     * For JsonPath See: https://github.com/jayway/JsonPath#getting-started
     *
     * @param expectedValue the expected string to find
     * @example AssertJsonElement ByPreviousFind in RestResponseBody is a 'string' with value: test string
     * @section Rest Assertion - JSON
     */
    //@Step("AssertJsonElement ByPreviousFind in RestResponseBody is a 'string' with value: (.*)")
    public void assertCurrentJsonElementByPreviousFindInRestResponseBodyIsAStringWithValue(final String expectedValue) {
        LOG.debug("Asserting that JsonElement from RestResponseBody is a string with value: " + expectedValue);

        // TODO - Work out how
        //final String currentJsonElement = getCurrentJsonElement(String.class);

        //assertThat(currentJsonElement, is(equalTo(expectedValue)));
    }

    /**
     * Assert that at the last find by JsonPath there is a number with the given value.
     * For JsonPath See: https://github.com/jayway/JsonPath#getting-started
     *
     * @param expectedValue the expected number to find
     * @example AssertJsonElement ByPreviousFind in RestResponseBody is a 'number' with value: 666.616
     * @section Rest Assertion - JSON
     */
    //@Step("AssertJsonElement ByPreviousFind in RestResponseBody is a 'number' with value: ([0-9]+(?:\\.[0-9]+)?)")
    public void assertCurrentJsonElementByPreviousFindInRestResponseBodyIsANumberWithValue(final double expectedValue) {
        LOG.debug("Asserting that JsonElement from RestResponseBody is a number with value: " + expectedValue);

        // TODO - Work out how
        //final double currentJsonElement = getCurrentJsonElement(Number.class).doubleValue();

        //assertThat(currentJsonElement, is(equalTo(expectedValue)));
    }

    /**
     * Assert that at the last find by JsonPath there is a boolean with the given value.
     * For JsonPath See: https://github.com/jayway/JsonPath#getting-started
     *
     * @param expectedValue the expected boolean to find
     * @example AssertJsonElement ByPreviousFind in RestResponseBody is a 'boolean' with value: true
     * @section Rest Assertion - JSON
     */
    //@Step("AssertJsonElement ByPreviousFind in RestResponseBody is a 'boolean' with value: ([tT][rR][uU][eE]|[fF][aA][lL][sS][eE])")
    public void assertCurrentJsonElementByPreviousFindInRestResponseBodyIsABooleanWithValue(final boolean expectedValue) {
        LOG.debug("Asserting that JsonElement from RestResponseBody is a boolean with value: " + expectedValue);

        // TODO - Work out how
        //final Boolean currentJsonElement = getCurrentJsonElement(Boolean.class);

        //assertThat(currentJsonElement, is(equalTo(expectedValue)));
    }
}
