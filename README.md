# Substeps RestDriver [![Build Status](https://travis-ci.org/beercan1989/substeps-restdriver.svg?branch=master)](https://travis-ci.org/beercan1989/substeps-restdriver)&nbsp;[![Maven Central](https://img.shields.io/maven-central/v/uk.co.baconi.substeps/restdriver-substeps.png)](https://maven-badges.herokuapp.com/maven-central/uk.co.baconi.substeps/restdriver-substeps)&nbsp;[![Codacy Badge](https://api.codacy.com/project/badge/grade/5c6e489d1b714798b474af0464af3945)](https://www.codacy.com/app/beercan1989/substeps-restdriver)&nbsp;[![codecov.io](https://codecov.io/github/beercan1989/substeps-restdriver/coverage.svg?branch=master)](https://codecov.io/github/beercan1989/substeps-restdriver?branch=master)

Project to provide a new type of Driver for use with Substeps, that will enable the ability to testing of rest endpoints by creating requests and asserting the responses.

+ Substeps Site: https://substeps.g2g3.digital
+ Substeps Sources: https://github.com/G2G3Digital?utf8=%E2%9C%93&query=substeps

## Basic Requirements
+ Java 8
+ Substeps 1.1.2

## Latest version available in Maven Central
```xml
<dependency>
    <groupId>uk.co.baconi.substeps</groupId>
    <artifactId>restdriver-substeps</artifactId>
    <version>0.0.2</version>
</dependency>
```

## Features that might be included in 0.0.3
+ More steps
    + Finders
    + Assertions
+ XML Support
+ Bug fixing
+ Other enhancements

## Features in 0.0.2
+ Corrections to the glossary metadata, without the fix we cannot build a glossary of the steps in projects that use this library.

## Features in 0.0.1
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
