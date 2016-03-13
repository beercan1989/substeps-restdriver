# Substeps RestDriver [![Build Status](https://travis-ci.org/beercan1989/substeps-restdriver.svg?branch=master)](https://travis-ci.org/beercan1989/substeps-restdriver)&nbsp;[![Maven Central](https://img.shields.io/maven-central/v/uk.co.baconi.substeps/restdriver-substeps.png)](https://maven-badges.herokuapp.com/maven-central/uk.co.baconi.substeps/restdriver-substeps)&nbsp;[![Codacy Grade Badge](https://api.codacy.com/project/badge/grade/5c6e489d1b714798b474af0464af3945)](https://www.codacy.com/app/beercan1989/substeps-restdriver)&nbsp;[![Codacy Coverage Badge](https://api.codacy.com/project/badge/coverage/5c6e489d1b714798b474af0464af3945)](https://www.codacy.com/app/beercan1989/substeps-restdriver)&nbsp;[![codecov.io](https://codecov.io/github/beercan1989/substeps-restdriver/coverage.svg?branch=master)](https://codecov.io/github/beercan1989/substeps-restdriver?branch=master)&nbsp;[![Coverage Status](https://coveralls.io/repos/beercan1989/substeps-restdriver/badge.svg?branch=master&service=github)](https://coveralls.io/github/beercan1989/substeps-restdriver?branch=master)

Project to provide a new type of Driver for use with Substeps, that will enable the ability to testing of rest endpoints by creating requests and asserting the responses.

+ Substeps Site: http://substeps.github.io
+ Substeps Sources: https://github.com/Substeps

## Basic Requirements
+ Java 8
+ Substeps 1.0.1

## Latest version available in Maven Central
```xml
<dependency>
    <groupId>uk.co.baconi.substeps</groupId>
    <artifactId>restdriver-substeps</artifactId>
    <version>0.0.3</version>
</dependency>
```

## Documentation
+ Available substeps: [docs/rest-driver-substeps.md](docs/rest-driver-substeps.md)

## Features that might be included in the future
+ XML Support
+ Bug fixing
+ Other enhancements

## Changes in 0.0.4
+ Moved to use the [org.substeps](https://github.com/Substeps/substeps-framework) fork of Substeps 
+ Setup project to generated and provide Substep implementation documentation
+ More ways to configure urls
+ Ways of asserting response times

## Changes in 0.0.3
+ More finders
+ More assertions
+ Move to using rest assured for the requesting and asserting

## Changes in 0.0.2
+ Corrections to the glossary metadata, without the fix we cannot build a glossary of the steps in projects that use this library.

## Changes in 0.0.1
+ Build rest requests.
+ Add headers in key value pairs.
+ Add cookies in key value pairs.
+ Add data to send in key/value pairs
+ Choose either a simple JSON or Key Pair builders to produce a suitable request body.
+ Set connect timeout, overriding defaults.
+ Set socket timeout, overriding defaults.
+ Set user agent, overriding defaults.
+ Defaults are stored in Typesafe Config properties.
+ Basic rest assertions:
    + Status Code
    + Status Code in Range
    + Reason Phrase
+ Basic JSON finding via JsonPath.
+ Basic JSON asserting via JsonPath and very basic types.
    + Object - only that it is one, haven't worked out best way to assert contents. 
    + Array - only that it is one, haven't worked out best way to assert contents.
    + String - both finding and matching, ignoring whitespace
    + Number - both finding and matching, including decimals
+ Examples / documentation on how to use.
+ Available in maven central

## Useful information
+ Scopes - can be the following values, they are thread safe scopes where data can be stored during substep runs:
    + SUITE
    + FEATURE
    + SCENARIO
    + SCENARIO_BACKGROUND
    + SCENARIO_OUTLINE
    + SCENARIO_OUTLINE_ROW
    + STEP
