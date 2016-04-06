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

package uk.co.baconi.substeps.restdriver.impl;

import org.springframework.http.ResponseEntity;
import uk.co.baconi.substeps.restdriver.impl.models.Result;
import uk.co.baconi.substeps.restdriver.impl.models.Stuff;
import uk.co.baconi.substeps.restdriver.impl.models.NumberPair;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class TestRestController {

    @RequestMapping(value = "/get-list-of-string", method = RequestMethod.GET)
    List<String> getListOfString() {
        final List<String> stuff = new ArrayList<>();
        stuff.add("Stuff 1");
        stuff.add("Stuff 2");
        stuff.add("Stuff 3");
        stuff.add("Stuff 4");
        stuff.add("Stuff 5");
        return stuff;
    }

    @RequestMapping(value = "/get-list-of-stuff", method = RequestMethod.GET)
    List<Stuff> getListOfStuff() {
        final List<Stuff> stuff = new ArrayList<>();
        stuff.add(new Stuff("Stuff 1"));
        stuff.add(new Stuff("Stuff 2"));
        stuff.add(new Stuff("Stuff 3"));
        stuff.add(new Stuff("Stuff 4"));
        stuff.add(new Stuff("Stuff 5"));
        return stuff;
    }

    @RequestMapping(value = "/get-stuff", method = RequestMethod.GET)
    Stuff getStuff() {
        return new Stuff("Stuff 6");
    }

    @RequestMapping(value = "/delete-success", method = RequestMethod.DELETE)
    void deleteSuccess() {
        // This constructor is intentionally empty as its just a test delete endpoint.
    }

    @RequestMapping(value = "/delete-failure", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void deleteFailure() {
        // This constructor is intentionally empty as its just a test delete endpoint.
    }

    @RequestMapping(value = "/post-sum", method = RequestMethod.POST)
    Result<Long> postSum(@RequestBody final NumberPair numbers) {
        return getSum(numbers.getFirst(), numbers.getSecond());
    }

    @RequestMapping(value = "/get-sum", method = RequestMethod.GET)
    Result<Long> getSum(final long first, final long second) {
        return new Result<>(first + second);
    }

    @RequestMapping(value = "/get-boolean", method = RequestMethod.GET, params = "value")
    Result<Boolean> getBoolean(@RequestParam("value") final boolean value) {
        return new Result<>(value);
    }

    @RequestMapping(value = "/get-number", method = RequestMethod.GET, params = "value")
    Result<Number> getBoolean(@RequestParam("value") final Number value) {
        return new Result<>(value);
    }

    @RequestMapping(value = "/get-string", method = RequestMethod.GET, params = "value")
    Result<String> getBoolean(@RequestParam("value") final String value) {
        return new Result<>(value);
    }

    @RequestMapping("/any")
    @ResponseStatus(HttpStatus.OK)
    void any() {
        // This constructor is intentionally empty as its just a test delete endpoint.
    }

    @RequestMapping("/replay-header")
    ResponseEntity<Void> replayHeader(@RequestHeader("replay") final String header) {
        return ResponseEntity.ok().header("replayed", header).build();
    }

    @RequestMapping("/replay-cookie")
    @ResponseStatus(HttpStatus.OK)
    Result<String> replayCookie(@CookieValue("replay") final String cookie) {
        return new Result<>(cookie);
    }

    @RequestMapping("/replay-param")
    @ResponseStatus(HttpStatus.OK)
    Result<String> replayParam(@RequestParam("replay") final String param) {
        return new Result<>(param);
    }

    @RequestMapping("/timed")
    @ResponseStatus(HttpStatus.OK)
    void replayParam(@RequestParam("wait-value") final Long value, @RequestParam("wait-unit") final TimeUnit unit) throws InterruptedException {
        unit.sleep(value);
    }

    @RequestMapping("/replay-json-array")
    @ResponseStatus(HttpStatus.OK)
    List<Map<String, String>> replayJsonArray(@RequestBody List<Map<String, String>> body) {
        return body;
    }
}
