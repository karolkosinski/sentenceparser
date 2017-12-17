# sentenceparser
## Introduction
This is a simple application for reading sentences from standard input and parsing them into xml or csv outputted to standard output.

## Building sentenceparser application
Source code of the application is supplied as a Maven project. Runnable application jar can be produced by invoking the following command:
```
mvn package
```

## Running sentenceparser application
The application can operate in one of two modes: `xml` or `csv` (`xml` is the default). Desired operation mode can be specified by command line parameter.
Examples:
* run application in `xml` default mode
```
java -jar sentenceparser.jar <file.in >file.out
```
```
java -jar sentenceparser.jar xml <file.in >file.out
```
* run application in `csv` mode
```
java -jar sentenceparser.jar csv <file.in >file.out
```
