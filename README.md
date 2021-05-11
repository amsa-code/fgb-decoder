# fgb-decoder

This is an extraction and large cleanup of AMSA's First Generation Beacon hexId and detection message decode java source code (originally in *aussar-oracle-java* project). 

**Status**: Not production ready!

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