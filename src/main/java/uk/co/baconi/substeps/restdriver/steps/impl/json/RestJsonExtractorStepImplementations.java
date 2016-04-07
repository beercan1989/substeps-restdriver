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
import uk.co.baconi.substeps.restdriver.steps.AbstractRestDriverSubStepImplementations;
import uk.co.baconi.substeps.restdriver.utils.ExecutionContextUtil;

import java.io.IOException;

@StepImplementations
public class RestJsonExtractorStepImplementations extends AbstractRestDriverSubStepImplementations {

    private static final Logger LOG = LoggerFactory.getLogger(RestJsonExtractorStepImplementations.class);

    private final RestJsonFinderStepImplementations finderImpl;

    public RestJsonExtractorStepImplementations() {
        finderImpl = new RestJsonFinderStepImplementations();
    }

    /**
     * Extract a json element as a string into a variable stored in the scenario scope.
     *
     * @param jsonPath the JsonPath to search with
     * @return the extracted json data
     * @throws IOException if the response body could not be read
     * @example ExtractJsonElement ByJsonPath 'id' as a 'string' into scenario variable 'SOME_ID'
     * @section Rest Extractor - JSON
     */
    @Step("ExtractJsonElement ByJsonPath '([^']+)' as a 'string' into scenario variable '([^']+)'")
    public String extractJsonElementByJsonPathAsAStringIntoScenarioVariable(final String jsonPath, final String variableName) throws IOException {

        final String extracted = finderImpl.findJsonElementByJsonPathInRestResponseBodyAString(jsonPath);

        ExecutionContextUtil.put(Scope.SCENARIO, variableName, extracted);

        return extracted;
    }

}