# DSV to JSONL Converter

## Overview
This Maven project converts Delimiter-Separated Values (DSV) files into JSON Lines (JSONL) format. It ensures dates in the JSONL output file are in `YYYY-MM-dd` format. The project is designed to handle large files efficiently by processing data in a streaming manner, avoiding the need to store all data entries in memory simultaneously.

## Features
- Converts DSV files with any delimiter to JSONL format.
- Dates are formatted in `YYYY-MM-dd` format in the JSONL output.
- Supports dynamic structures for DSV files.
- Processes files in a streaming manner using the Java Stream API.
- Command-line interface for specifying input files and additional parameters.
- Unit tests included to verify functionality with provided test input files.

## Usage

### 1. Build the Project
```sh
mvn package
```
### 2. Run the Converter
```sh
java -jar target/DsvToJsonlConverter-1.0-SNAPSHOT.jar <inputFilePath> <outputFilePath> <delimiter>
```

## Example
```sh
java -jar target/DsvToJsonlConverter-1.0-SNAPSHOT.jar src/main/resources/files/DSV_input1.txt src/main/resources/files/output.json ','
```