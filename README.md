# fgb-decoder
<a href="https://github.com/amsa-code/fgb-decoder/actions/workflows/ci.yml"><img src="https://github.com/amsa-code/fgb-decoder/actions/workflows/ci.yml/badge.svg"/></a><br/>
[![codecov](https://codecov.io/gh/amsa-code/fgb-decoder/branch/master/graph/badge.svg)](https://codecov.io/gh/amsa-code/fgb-decoder)<br/>
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/au.gov.amsa/fgb-decoder/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/au.gov.amsa/fgb-decoder)<br/>

This is an extraction and large cleanup of AMSA's COSPAS-SARSAT First Generation Beacon hexId and detection message decode java source code (originally in *aussar-oracle-java* project). The decode is compliant with the C/S T.001 specification Issue 4 Revision 7 March 2021.

The cleanup included:
* use JSON types like int, double, boolean instead of pure text fields (YES = true etc)
* search through 100,000 historical FGB messages to increase test coverage to over 90%
* create enumerated types
* a large refactoring to deal with a huge amount of copy-and-paste!

There's plenty of legacy ugliness in there still but should be enough for AMSA's use cases.

**Status**: In development, will be in use in production in AMSA by July 2021

Maven [reports](https://amsa-code.github.io/fgb-decoder/index.html) including [javadocs](https://amsa-code.github.io/fgb-decoder/apidocs/index.html)

**See also**: [sgb-decoder](https://github.com/amsa-code/sgb-decoder.git) to decode Second Generation Beacon Hex Ids and detection messages.

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
## Examples
The hexId `ADCC40504000185` decodes to:
```json
{
"messageType": "User (Format - Unknown)",
"hexData": "Unknown",
"hexId": "ADCC40504000185",
"countryCode": 366,
"userProtocolType": "Serial",
"beaconType": "Aviation",
"cSCertNumberPresent": true,
"serialNumber": 5136,
"nationalUse": "0000000000",
"cSTypeApprovalNumber": 97,
"auxiliaryRadioLocatingDevice": "121.5 MHz",
"uSManufacturerId": 1,
"uSSequenceNumber": 1040,
"uSModelId": 0,
"uSRunNumber": 0,
"uSNationalUse": "01100001"
}
```

The detection message `D6E6202820000C29FF51041775302D` decodes to:

```json
{
"messageType": "User Location (Long)",
"hexData": "D6E6202820000C29FF51041775302D",
"hexId": "ADCC40504000185",
"countryCode": 366,
"userProtocolType": "Serial",
"beaconType": "Aviation",
"cSCertNumberPresent": true,
"serialNumber": 5136,
"nationalUse": "0000000000",
"cSTypeApprovalNumber": 97,
"auxiliaryRadioLocatingDevice": "121.5 MHz",
"uSManufacturerId": 1,
"uSSequenceNumber": 1040,
"uSModelId": 0,
"uSRunNumber": 0,
"uSNationalUse": "01100001",
"errorCorrectingCode1": "001111111110101000100",
"encodedPositionSource": "EXTERNAL",
"latitude": 32.733333333333334,
"longitude": -117.2,
"errorCorrectingCode2": "000000101101"
}
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
* `git checkout <VERSION> && ./generate-site.sh` (requires repo *amsa-code.github.io* to be sitting next to fgb-decoder)
* change the *fgb-decoder* version in *beacon-decoder-aws* and run its release script (which deploys it to AWS)
* change the *fgb-decoder* version in *parent/beacon-decoder* and redeploy it to Tomcat on sar*.
