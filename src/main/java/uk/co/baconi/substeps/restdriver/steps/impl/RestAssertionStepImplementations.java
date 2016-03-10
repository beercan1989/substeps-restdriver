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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.baconi.substeps.restdriver.RestDriverSetupAndTearDown;
import uk.co.baconi.substeps.restdriver.steps.AbstractRestDriverSubStepImplementations;
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepImplementations;
import org.apache.http.StatusLine;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@StepImplementations(requiredInitialisationClasses = RestDriverSetupAndTearDown.class)
public class RestAssertionStepImplementations extends AbstractRestDriverSubStepImplementations {

    private static final Logger LOG = LoggerFactory.getLogger(RestAssertionStepImplementations.class);

    /**
     * Check that the rest response has the expected http status response code.
     *
     * @param statusCode the expected status code
     * @example AssertRestResponse has code '200'
     * @section Rest Assertion
     */
    @Step("AssertRestResponse has code '([0-9]{3})'")
    public void assertRestResponseHasCode(final int statusCode) {

        LOG.debug("Asserting that the response code is a [{}]", statusCode);

        getResponse().statusCode(is(equalTo(statusCode)));
    }

    /**
     * Check that the rest response has the expected http status response code with expected reason.
     *
     * @param statusCode the expected status code
     * @param reason     the expected reason
     * @example AssertRestResponse has code '200' with reason 'OK'
     * @section Rest Assertion
     */
    @Step("AssertRestResponse has code '([0-9]{3})' with reason '([^']+)'")
    public void assertRestResponseHasCodeWithReason(final int statusCode, final String reason) {

        LOG.debug("Asserting that the response code is a [{}] with reason [{}]", statusCode, reason);

        getResponse().statusCode(is(equalTo(statusCode)));
        getResponse().statusLine(is(equalTo(reason)));
    }

    /**
     * Check that the rest response has the expected http status is within the given range.
     *
     * @param statusMin the minimum expected status code
     * @param statusMax the maximum expected status code
     * @example AssertRestResponse has code between '200' and '299'
     * @section Rest Assertion
     */
    @Step("AssertRestResponse has code between '([0-9]{3})' and '([0-9]{3})'")
    public void assertRestResponseHasCodeBetween(final int statusMin, final int statusMax) {

        LOG.debug("Asserting that the response code is between [{}] and [{}]", statusMin, statusMax);

        getResponse().statusCode(is(both(greaterThanOrEqualTo(statusMin)).and(lessThanOrEqualTo(statusMax))));
    }

    /**
     * Check that the rest response has the expected http header with the given value.
     *
     * @param headerName the header name
     * @param headerValue the header value
     * @example AssertRestResponse has header of name 'name' with value 'bob'
     * @section Rest Assertion
     */
    @Step("AssertRestResponse has header of name '([^']+)' with value '([^']*)'")
    public void assertResponseHasHeaderWithValue(final String headerName, final String headerValue) {

        LOG.debug("Asserting that there is a response header of name [{}] with value [{}]", headerName, headerValue);

        getResponse().header(headerName, headerValue);
    }

    /**
     * Check that the rest response has the expected http header with a predetermined state.
     *
     * @param headerName the header name
     * @param headerValueState the predetermined state for the value
     * @example AssertRestResponse has header of name 'name' with 'any' value
     * @section Rest Assertion
     */
    @Step("AssertRestResponse has header of name '([^']+)' with '(any|no|blank)' value")
    public void assertResponseHasHeader(final String headerName, final String headerValueState) {

        LOG.debug("Asserting that there is a response header of name [{}], with built in checking for [{}]", headerName, headerValueState);

        switch (headerValueState) {
            case "any": {
                getResponse().header(headerName, is(not(nullValue())));
                break;
            }
            case "no": {
                getResponse().header(headerName, is(nullValue()));
                break;
            }
            case "blank": {
                getResponse().header(headerName, isEmptyString());
            }
            default: {
                throw new AssertionError("Unsupported Type ["+headerValueState+"]");
            }
        }
    }
}
