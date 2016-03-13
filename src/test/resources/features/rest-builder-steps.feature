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

    RestRequest perform 'GET' on '/timed'

    AssertRestResponse has code '200'

    AssertRestResponse took > 3 MILLISECONDS
    AssertRestResponse took >= 5 NANOSECONDS

    AssertRestResponse took < 1 MINUTES
    AssertRestResponse took <= 2 SECONDS
    AssertRestResponse took <= 600 MILLISECONDS
