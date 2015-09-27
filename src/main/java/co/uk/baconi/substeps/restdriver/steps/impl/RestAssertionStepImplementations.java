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
import co.uk.baconi.substeps.restdriver.steps.AbstractRestDriverSubStepImplementations;
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepImplementations;
import org.apache.http.StatusLine;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@StepImplementations(requiredInitialisationClasses = RestDriverSetupAndTearDown.class)
public class RestAssertionStepImplementations extends AbstractRestDriverSubStepImplementations {

    /**
     * Check that the rest response has the expected http status response code.
     *
     * @param expectedStatusCode the expected status code
     * @example AssertRestResponse has code '200'
     * @section Rest Assertion
     */
    @Step("AssertRestResponse has code '([0-9]{3})'")
    public void assertRestResponseHasCode(final int expectedStatusCode) {

        logger.debug("Asserting that the response code is a [" + expectedStatusCode + "]");

        final int actualStatusCode = getResponse().getStatusLine().getStatusCode();

        assertThat(actualStatusCode, is(equalTo(expectedStatusCode)));
    }

    /**
     * Check that the rest response has the expected http status response code with expected reason.
     *
     * @param expectedStatusCode the expected status code
     * @param expectedReason     the expected reason
     * @example AssertRestResponse has code '200' with reason 'OK'
     * @section Rest Assertion
     */
    @Step("AssertRestResponse has code '([0-9]{3})' with reason '([^']+)'")
    public void assertRestResponseHasCodeWithReason(final int expectedStatusCode, final String expectedReason) {

        logger.debug("Asserting that the response code is a [" + expectedStatusCode + "] with reason [" + expectedReason + "]");

        final StatusLine statusLine = getResponse().getStatusLine();
        final int actualStatusCode = statusLine.getStatusCode();
        final String reasonPhrase = statusLine.getReasonPhrase();

        assertThat(actualStatusCode, is(equalTo(expectedStatusCode)));
        assertThat(reasonPhrase, is(equalTo(expectedReason)));
    }

    /**
     * Check that the rest response has the expected http status is within the given range.
     *
     * @param expectedMin the minimum expected status code
     * @param expectedMax the manimum expected status code
     * @example AssertRestResponse has code between '200' and '299'
     * @section Rest Assertion
     */
    @Step("AssertRestResponse has code between '([0-9]{3})' and '([0-9]{3})'")
    public void assertRestResponseHasCodeBetween(final int expectedMin, final int expectedMax) {

        logger.debug("Asserting that the response code is between [" + expectedMin + "] and [" + expectedMax + "]");

        final int actualStatusCode = getResponse().getStatusLine().getStatusCode();

        assertThat(actualStatusCode, is(both(greaterThanOrEqualTo(expectedMin)).and(lessThanOrEqualTo(expectedMax))));
    }

}
