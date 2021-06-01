# fgb-decoder

This is an extraction and large cleanup of AMSA's First Generation Beacon hexId and detection message decode java source code (originally in *aussar-oracle-java* project).

The cleanup included:
* use JSON types like int, double, boolean instead of pure text fields (YES = true etc)
* search through 100,000 historical FGB messages to increase test coverage to over 90%
* create enumerated types

There's plenty of legacy ugliness in there still but should be enough for AMSA's use cases.

**Status**: In development, will be in use in production in AMSA by July 2021

## Getting started
Add this dependency to your pom.xml:

```xml
<dependency>
  <groupId>au.gov.amsa</groupId>
  <artifactId>fgb-decoder</artifactId>
  <version>VERSION_HERE</version>
</dependency>
```

## Usage
To decode a 15 character hex beacon identifier:

```java
String json = Beacon15HexId.decodeHexToJson(hex);
```

To decode a 30 character hex beacon detection message:

```java
String json = Detection.decodeHexToJson(hex);
```

## How to build
```bash
mvn clean install
```

## Developer instructions
To build a release:
* `git fetch --tags && git tag` to choose the next version
* `./release.sh <VERSION>`
* go to Github project and publish a release (look at previous releases to see what goes in the release description field)
* in the Github project go to Actions and check to see if the release has made its way to Maven Central successfully
* change the *fgb-decoder* version in *beacon-decoder-aws* and run its release script (which deploys it to AWS)
* change the *fgb-decoder* version in *parent/beacon-decoder* and redeploy it to Tomcat on sar*.

