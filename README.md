## Java implementation of [Identifiers spec](https://github.com/Identifiers/spec)

[![Build Status](https://travis-ci.org/Identifiers/identifiers-java.svg?branch=master)](https://travis-ci.org/Identifiers/identifiers-java)
[![Coverage Status](https://coveralls.io/repos/github/Identifiers/identifiers-java/badge.svg?branch=master)](https://coveralls.io/github/Identifiers/identifiers-java?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/io.identifiers/identifiers.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.identifiers%22%20AND%20a:%22identifiers%22)

#### Installing the Identifiers Library

Maven Central coordinates:

```
<dependency>
   <groupId>io.identifiers</groupId>
   <artifactId>identifiers</artifactId>
   <version>0.1.0</version>
</dependency>
```

### Usage

Identifiers comes with a set of static factory methods to encode Identifier instances and decode these encoded strings.

```java
import io.identifiers.Factory;
import io.identifiers.Identifier;

Identifier<String> stringId = Factory.forString.create("a string value");

String encodedDataId = stringId.toDataString(); // smaller, good for data storage and transmission
String encodedHumanId = stringId.toHumanString(); // good for human interaction like emails and URLs

Identifier<String> decodedStringId = Factory.decodeFromString(encodedDataId);
// also decodes human strings
decodedStringId = Factory.decodeFromString(encodedHumanId);
```

Factories are provided for the following identifier types:

* string
* boolean
* integer (32-bit signed ints)
* float (64-bit signed decimals)
* long (64-bit signed ints)
* bytes
* UUID (any version)
* Datetime (Java Instant type)
* Geo (decimal latitude / longitude)

#### Structured Identifiers
All the factory methods come with List and Map factory methods to create typed structured identifiers.

```java
import io.identifiers.Factory;
import io.identifiers.Identifier;

// For datetime IDs
import java.time.Instant;

// List identifiers are declared as generic Lists.
ListIdentifier<Boolean> booleanListId = Factory.forString.createList(true, false);

Map<String, Instant>> dates = new HashMap<>();
dates.put("before", Instant.parse("2010-01-01"));
dates.put("after", Instant.parse("2011-12-31"));

// Map identifiers are declared as generic Maps with String keys.
MapIdentifier<Instant> Factory.forDatetime.createMap(dates);
```

#### Composite Identifiers

Different types of identifiers can be combined into a composite identifier. They can be composed as either Lists or Maps.

```java
import io.identifiers.Factory;
import io.identifiers.Identifier;

ListIdentifier<Identifier<?>> compositeListId = Factory.forComposite.createList(
	Factory.string.create("s1"),
	Factory.float.createList(22.1, 6543.87),
	Factory.boolean.createMap(java.util.Collections.singletonMap("flag", true)));
```