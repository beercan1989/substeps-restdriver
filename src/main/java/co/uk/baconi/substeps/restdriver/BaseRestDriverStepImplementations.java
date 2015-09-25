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

package co.uk.baconi.substeps.restdriver;

import co.uk.baconi.substeps.restdriver.steps.impl.RestAssertionStepImplementations;
import co.uk.baconi.substeps.restdriver.steps.impl.RestRequestBuilderStepImplementations;
import co.uk.baconi.substeps.restdriver.steps.impl.json.RestJsonAssertionStepImplementations;
import co.uk.baconi.substeps.restdriver.steps.impl.json.RestJsonFinderStepImplementations;
import com.technophobia.substeps.model.SubSteps.AdditionalStepImplementations;
import com.technophobia.substeps.model.SubSteps.StepImplementations;

@StepImplementations(requiredInitialisationClasses = RestDriverSetupAndTearDown.class)
@AdditionalStepImplementations({
        RestRequestBuilderStepImplementations.class,
        RestAssertionStepImplementations.class,
        RestJsonAssertionStepImplementations.class,
        RestJsonFinderStepImplementations.class
})
public class BaseRestDriverStepImplementations {
}