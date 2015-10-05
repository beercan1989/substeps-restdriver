/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.baconi.substeps.restdriver;

import uk.co.baconi.substeps.restdriver.impl.TestServerSetup;
import com.technophobia.substeps.runner.JunitFeatureRunner;
import com.technophobia.substeps.runner.JunitFeatureRunner.SubStepsConfiguration;
import org.junit.runner.RunWith;

@SubStepsConfiguration(
        featureFile = "./target/test-classes/features",
        subStepsFile = "./target/test-classes/substeps",
        stepImplementations = {
                BaseRestDriverStepImplementations.class,
                TestServerSetup.class
        }
)
@RunWith(JunitFeatureRunner.class)
public class TestRunner {
}
