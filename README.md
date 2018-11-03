## Java implementation of [Identifiers spec](https://github.com/Identifiers/spec)

[![Build Status](https://travis-ci.org/Identifiers/identifiers-java.svg?branch=master)](https://travis-ci.org/Identifiers/identifiers-java)

#### Installing the Identifiers Library

For Maven users:

```
<dependency>
   <groupId>io.identifiers</groupId>
   <artifactId>identifiers</artifactId>
   <version>(version)</version>
</dependency>
```

For sbt users:

```
libraryDependencies += "io.identifiers" % "identifiers" % "(version)"
```

For gradle users:

```
repositories {
    mavenCentral()
}

dependencies {
    compile 'io.identifiers:identifiers:(version)'
}
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
Identifier<List<Boolean>> booleanListId = Factory.forString.createList(true, false);

Map<String, Instant>> dates = new HashMap<>();
dates.put("before", Instant.parse("2010-01-01"));
dates.put("after", Instant.parse("2011-12-31"));

// Map identifiers are declared as generic Maps with String keys.
Identifier<Map<String, Instant>> Factory.forDatetime.createMap(dates);
```

#### Composite Identifiers

Different types of identifiers can be combined into a composite identifier. They can be composed as either Lists or Maps.

```java
import io.identifiers.Factory;
import io.identifiers.Identifier;

Identifier<List<Identifier<?>> compositeListId = Factory.forComposite.createList(
	Factory.string.create("s1"),
	Factory.float.createList(22.1, 6543.87),
	Factory.bollean.createMap(java.util.Collections.singletonMap("flag", true)));
```