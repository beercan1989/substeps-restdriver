Tags: @non-visual

Feature: A feature to test lots of the request builder

Scenario Outline: A scenario to test each method type
    RestRequest setup new request
    RestRequest perform '<method>' on '/any'
    AssertRestResponse has code '200'

Examples:
    |method |
    |DELETE |
    |GET    |
    |HEAD   |
    |OPTIONS|
    |PATCH  |
    |POST   |
    |PUT    |

Scenario: A scenario to test hidden gems
    RestRequest setup new request
    RestRequest add header with name 'replay' and value '1234567890'
    RestRequest set user-agent string as 'SubstepsRestDriver/0.0.3 (+https://github.com/beercan1989/substeps-restdriver)'

    RestRequest perform 'GET' on 'http://localhost:9000/replay-header'

    AssertRestResponse has code between '200' and '299'
    AssertRestResponse has header of name 'replayed' with value '1234567890'
    AssertRestResponse has header of name 'replayed' with 'any' value
    AssertRestResponse has header of name 'replayed' with 'non-blank' value

Scenario: A scenario to test other hidden gems
    RestRequest setup new request
    RestRequest add cookie with name 'replay' and value 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'

    RestRequest perform 'GET' on 'http://localhost:9000/replay-cookie'

    AssertRestResponse has code '200' with reason 'HTTP/1.1 200 OK'
    AssertRestResponse has header of name 'replayed' with 'no' value
    AssertJsonElement ByJsonPath 'result' in RestResponseBody a 'string' with value: ABCDEFGHIJKLMNOPQRSTUVWXYZ

Scenario: A scenario to test replying params
    RestRequest setup new request
    RestRequest add param with name 'replay' and value '0987654321'

    RestRequest perform 'GET' on '/replay-param'

    AssertRestResponse has code '200'
    AssertJsonElement ByJsonPath 'result' in RestResponseBody a 'string' with value: 0987654321

Scenario: A scenario to test response times
    RestRequest setup new request
    RestRequest add param with name 'wait-value' and value '500'
    RestRequest add param with name 'wait-unit' and value 'MILLISECONDS'

    RestRequest perform 'GET' on '/timed/'

    AssertRestResponse has code '200'

    AssertRestResponse took > 3 MILLISECONDS
    AssertRestResponse took >= 5 NANOSECONDS

    AssertRestResponse took < 1 MINUTES
    AssertRestResponse took <= 2 SECONDS
    AssertRestResponse took <= 600 MILLISECONDS
    AssertRestResponse took < 1 MINUTES
    AssertRestResponse took < 1 HOURS
    AssertRestResponse took < 1 DAYS
    AssertRestResponse took between 500 and 600 MILLISECONDS

Scenario: A scenario to post a list of json objects
    RestRequest setup new request
    RestRequest build body using the 'JsonArrayRequestBodyBuilder'

    RestRequest add data at position '0' with name 'key1' and value 'value1'
    RestRequest add data at position '0' with name 'key2' and value 'value2'
    RestRequest add data at position '1' with name 'key1' and value 'value3'
    RestRequest add data at position '1' with name 'key2' and value 'value4'

    RestRequest perform 'POST' on '/replay-json-array'

    AssertRestResponse has code '200'
    AssertRestResponseBody is JSON 'array'
    AssertJsonElement ByJsonPath '[0]' in RestResponseBody an 'object'
    AssertJsonElement ByJsonPath '[1]' in RestResponseBody an 'object'
    AssertJsonElement ByJsonPath '[0].key1' in RestResponseBody a 'string' with value: value1
    AssertJsonElement ByJsonPath '[0].key2' in RestResponseBody a 'string' with value: value2
    AssertJsonElement ByJsonPath '[1].key1' in RestResponseBody a 'string' with value: value3
    AssertJsonElement ByJsonPath '[1].key2' in RestResponseBody a 'string' with value: value4

Scenario: A scenario to post a json file
    RestRequest setup new request
    RestRequest build body using the 'JsonFromUriRequestBodyBuilder'
    RestRequest add data from resource file 'json/some.json'
    RestRequest perform 'POST' on '/replay-json-array'

    AssertRestResponse has code '200'
    AssertRestResponseBody is JSON 'array'
    AssertJsonElement ByJsonPath '[0]' in RestResponseBody an 'object'
    AssertJsonElement ByJsonPath '[1]' in RestResponseBody an 'object'
    AssertJsonElement ByJsonPath '[0].key1' in RestResponseBody a 'string' with value: value1
    AssertJsonElement ByJsonPath '[0].key2' in RestResponseBody a 'string' with value: value2
    AssertJsonElement ByJsonPath '[1].key1' in RestResponseBody a 'string' with value: value3
    AssertJsonElement ByJsonPath '[1].key2' in RestResponseBody a 'string' with value: value4

Scenario: A scenario to post a json url
    RestRequest setup new request
    RestRequest build body using the 'JsonFromUriRequestBodyBuilder'
    RestRequest add data from url 'https://raw.githubusercontent.com/beercan1989/substeps-restdriver/master/src/test/resources/json/some.json'
    RestRequest perform 'POST' on '/replay-json-array'

    AssertRestResponse has code '200'
    AssertRestResponseBody is JSON 'array'
    AssertJsonElement ByJsonPath '[0]' in RestResponseBody an 'object'
    AssertJsonElement ByJsonPath '[1]' in RestResponseBody an 'object'
    AssertJsonElement ByJsonPath '[0].key1' in RestResponseBody a 'string' with value: value1
    AssertJsonElement ByJsonPath '[0].key2' in RestResponseBody a 'string' with value: value2
    AssertJsonElement ByJsonPath '[1].key1' in RestResponseBody a 'string' with value: value3
    AssertJsonElement ByJsonPath '[1].key2' in RestResponseBody a 'string' with value: value4
