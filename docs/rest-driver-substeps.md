Miscellaneous
==========
| **Keyword**  | **Example**  | **Description** |
| :------------ |:---------------| :-----|
| RestRequest add data from resource file '&lt;resourceFile&gt;' |  |  |
| RestRequest add data from url '&lt;url&gt;' |  |  |
Rest Assertion
==========
| **Keyword**  | **Example**  | **Description** |
| :------------ |:---------------| :-----|
| AssertRestResponse has code '&lt;statusCode&gt;' | AssertRestResponse has code '200' | Check that the rest response has the expected http status response code. |
| AssertRestResponse has code '&lt;statusCode&gt;' with reason '&lt;reason&gt;' | AssertRestResponse has code '200' with reason 'OK' | Check that the rest response has the expected http status response code with expected reason. |
| AssertRestResponse has code between '&lt;statusMin&gt;' and '&lt;statusMax&gt;' | AssertRestResponse has code between '200' and '299' | Check that the rest response has the expected http status is within the given range. |
| AssertRestResponse has header of name '&lt;headerName&gt;' with '&lt;headerValueState&gt;' value | AssertRestResponse has header of name 'name' with 'any' value | Check that the rest response has the expected http header with a predetermined state. |
| AssertRestResponse has header of name '&lt;headerName&gt;' with value '&lt;headerValue&gt;' | AssertRestResponse has header of name 'name' with value 'bob' | Check that the rest response has the expected http header with the given value. |
Rest Assertion - JSON
==========
| **Keyword**  | **Example**  | **Description** |
| :------------ |:---------------| :-----|
| AssertJsonElement ByJsonPath '&lt;jsonPath&gt;' in RestResponseBody &lt;aOrAn&gt; '&lt;type&gt;' | AssertJsonElement ByJsonPath '$.something.someArray' in RestResponseBody an 'array' | Assert that at the given JsonPath there is the given base JSON type.  For JsonPath See: https://github.com/jayway/JsonPath#getting-started |
| AssertJsonElement ByJsonPath '&lt;jsonPath&gt;' in RestResponseBody a 'boolean' with value: &lt;expectedValue&gt; | AssertJsonElement ByJsonPath '$.someBoolean' in RestResponseBody a 'boolean' with value: false | Assert that at the given JsonPath there is a boolean with the given value.  For JsonPath See: https://github.com/jayway/JsonPath#getting-started |
| AssertJsonElement ByJsonPath '&lt;jsonPath&gt;' in RestResponseBody a 'number' with value: &lt;expectedValue&gt; | AssertJsonElement ByJsonPath '$.someNumber' in RestResponseBody a 'number' with value: 666 | Assert that at the given JsonPath there is a number with the given value.  For JsonPath See: https://github.com/jayway/JsonPath#getting-started |
| AssertJsonElement ByJsonPath '&lt;jsonPath&gt;' in RestResponseBody a 'string' contains value: &lt;expectedValue&gt; | AssertJsonElement ByJsonPath 'someString' in RestResponseBody a 'string' contains value: test | Assert that at the given JsonPath there is a string that contains the given value.  For JsonPath See: https://github.com/jayway/JsonPath#getting-started |
| AssertJsonElement ByJsonPath '&lt;jsonPath&gt;' in RestResponseBody a 'string' with value in scenario variable: &lt;variableName&gt; | AssertJsonElement ByJsonPath 'someString' in RestResponseBody a 'string' with value in scenario variable: TEST | Assert that at the given JsonPath there is a string that contains the value stored in the scenario variable.  For JsonPath See: https://github.com/jayway/JsonPath#getting-started |
| AssertJsonElement ByJsonPath '&lt;jsonPath&gt;' in RestResponseBody a 'string' with value: &lt;expectedValue&gt; | AssertJsonElement ByJsonPath '$.someString' in RestResponseBody a 'string' with value: test string | Assert that at the given JsonPath there is a string with the given value.  For JsonPath See: https://github.com/jayway/JsonPath#getting-started |
| AssertRestResponseBody is JSON '&lt;type&gt;' | AssertRestResponseBody is JSON 'object' | Assert that the rest response body is the given JSON base type.  For JsonPath See: https://github.com/jayway/JsonPath#getting-started |
Rest Builder
==========
| **Keyword**  | **Example**  | **Description** |
| :------------ |:---------------| :-----|
| RestRequest add cookie with name '&lt;name&gt;' and value '&lt;value&gt;' | RestRequest add cookie with name 'JESSIONID' and value 'ABCDEFGHIJKLMNOPQRSTUVWXYZ' | Add a cookie to the current rest request being built with the given name and value |
| RestRequest add cookie with name '&lt;name&gt;' and value '&lt;value&gt;' in scope '&lt;scope&gt;' | RestRequest add cookie with name 'JESSIONID' and value 'ABCDEFGHIJKLMNOPQRSTUVWXYZ' in scope 'FEATURE' | Add a cookie to the given scope with the given name and value, and will be used in any request until the scope expires. |
| RestRequest add data at position '&lt;position&gt;' with name '&lt;name&gt;' and value '&lt;value&gt;' | RestRequest add data at position '0' with name 'username' and value 'bob' | Add data to the current request being build, at a position with name and value provided. |
| RestRequest add data with name '&lt;name&gt;' and value '&lt;value&gt;' | RestRequest add data with name 'name' and value 'my_name' | Add data to the current request being built, with the name and value provided. |
| RestRequest add header with name '&lt;name&gt;' and value '&lt;value&gt;' | RestRequest add header with name 'api_key' and value '1234567890' | Add a header to the current rest request being built with the given name and value |
| RestRequest build body using the '&lt;builder&gt;' | NewRestRequestBody using the 'JsonObjectRequestBodyBuilder' | Select the type of rest request body builder to be used in the current scenario. Currently there is only support  for JsonObjectRequestBodyBuilder (key pairs in json format) and FormRequestBodyBuilder (form submission format). |
| RestRequest perform '&lt;method&gt;' on '&lt;url&gt;' | NewRestRequest as 'GET' to '/get-stuff' | Create a new rest request using the given HTTP method and URL. The URL can either be absolute or relative to the  base url in the properties. |
| RestRequest set proxy as '&lt;proxy&gt;' | RestRequest set proxy as 'http://localhost:616' | Set the proxy for the current rest request. |
| RestRequest set user-agent string as '&lt;userAgent&gt;' | RestRequest set user-agent string as 'SubstepsRestDriver/0.0.1 (+https://github.com/beercan1989/substeps-restdriver)' | Set the user agent string for the current rest request. |
| RestRequest setup new request | RestRequest setup new request | Setups up a new rest request and throws away any that are in the current scenario scope. |
Rest Builder - Param
==========
| **Keyword**  | **Example**  | **Description** |
| :------------ |:---------------| :-----|
| RestRequest add param with name '&lt;name&gt;' and value '&lt;value&gt;' | RestRequest add param with name 'name' and value 'bob' | Add a parameter to the url for the current rest request being built with the given name and value |
| RestRequest add path param with name '&lt;name&gt;' and scenario variable '&lt;variableName&gt;' | RestRequest add path param with name 'named-path-param' and scenario variable 'variable-name' | Add a named path parameter for the url on the current rest request being built with the given name and scenario variable value |
| RestRequest add path param with name '&lt;name&gt;' and value '&lt;value&gt;' | RestRequest add path param with name 'named-path-param' and value 'bob' | Add a named path parameter for the url on the current rest request being built with the given name and value |
Rest Finder - JSON
==========
| **Keyword**  | **Example**  | **Description** |
| :------------ |:---------------| :-----|
| FindJsonElement ByJsonPath '&lt;jsonPath&gt;' in RestResponseBody a 'boolean' | FindJsonElement ByJsonPath '$.someBoolean' in RestResponseBody a 'boolean' | Find a JSON boolean by the given JsonPath and store it for further inspection.  For JsonPath See: https://github.com/jayway/JsonPath#getting-started |
| FindJsonElement ByJsonPath '&lt;jsonPath&gt;' in RestResponseBody a 'number' | FindJsonElement ByJsonPath '$.someNumber' in RestResponseBody a 'number' | Find a JSON number by the given JsonPath and store it for further inspection.  For JsonPath See: https://github.com/jayway/JsonPath#getting-started |
| FindJsonElement ByJsonPath '&lt;jsonPath&gt;' in RestResponseBody a 'string' | FindJsonElement ByJsonPath '$.someString' in RestResponseBody a 'string' | Find a JSON string by the given JsonPath and store it for further inspection.  For JsonPath See: https://github.com/jayway/JsonPath#getting-started |
| FindJsonElement ByJsonPath '&lt;jsonPath&gt;' in RestResponseBody an 'array' | FindJsonElement ByJsonPath '$.someArray' in RestResponseBody an 'array' | Find a JSON array by the given JsonPath and store it for further inspection.  For JsonPath See: https://github.com/jayway/JsonPath#getting-started |
| FindJsonElement ByJsonPath '&lt;jsonPath&gt;' in RestResponseBody an 'object' | FindJsonElement ByJsonPath '$.someObject' in RestResponseBody an 'object' | Find a JSON object by the given JsonPath and store it for further inspection.  For JsonPath See: https://github.com/jayway/JsonPath#getting-started |
</table></body></html>