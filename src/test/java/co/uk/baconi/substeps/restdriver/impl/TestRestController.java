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

package co.uk.baconi.substeps.restdriver.impl;

import co.uk.baconi.substeps.restdriver.impl.models.Result;
import co.uk.baconi.substeps.restdriver.impl.models.Stuff;
import co.uk.baconi.substeps.restdriver.impl.models.NumberPair;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    }

    @RequestMapping(value = "/delete-failure", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void deleteFailure() {
    }

    @RequestMapping(value = "/post-sum", method = RequestMethod.POST)
    Result<Long> postSum(@RequestBody final NumberPair numbers) {
        return getSum(numbers.getFirst(), numbers.getSecond());
    }

    @RequestMapping(value = "/get-sum", method = RequestMethod.GET)
    Result<Long> getSum(final long first, final long second) {
        return new Result<>(first + second);
    }

    @RequestMapping(value = "/get-false", method = RequestMethod.GET)
    Result<Boolean> getFalse() {
        return new Result<>(false);
    }

    @RequestMapping(value = "/get-true", method = RequestMethod.GET)
    Result<Boolean> getTrue() {
        return new Result<>(true);
    }

}
