package org.pst.ag.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.pst.ag.constant.DateTimePatternConstant;
import org.pst.ag.service.DsvToJsonlConverter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

@Slf4j
public class DsvToJsonlDsvToJsonlConverterImpl implements DsvToJsonlConverter {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String DATE_FORMAT_PATTERN = "(\\d{2}[-/]\\d{2}[-/]\\d{4})|(\\d{4}[-/]\\d{2}[-/]\\d{2})";
    private static final DateTimeFormatter OUTPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern(DateTimePatternConstant.DATE_PATTERN_2);

    @Override
    public void convert(String inputFile, String outputFile, char delimiter) throws IOException {
        log.info("Started converting file {} to {}", inputFile, outputFile);
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             FileWriter writer = new FileWriter(outputFile)) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(delimiter).withFirstRecordAsHeader());
            StreamSupport.stream(csvParser.spliterator(), false)
                    .map(record -> toData(record, csvParser.getHeaderNames()))
                    .forEach(jsonNode -> writeJsonNode(writer, jsonNode));
            log.info("File conversion completed successfully");
        }
    }

    private static ObjectNode toData(CSVRecord record, Iterable<String> headers) {
        ObjectNode jsonNode = objectMapper.createObjectNode();
        for (String header : headers) {
            String value = record.get(header);
            // Skip empty strings or null values
            if (value == null || value.isEmpty()) {
                continue;
            }
            // Check if the value is a valid date format
            if (isValidDateFormat(value)) {
                jsonNode.put(header, formatDateString(value));
            }
            // Check if the value is a number
            else if (isNumeric(value)) {
                // Check if the value is an integer
                if (isInteger(value)) {
                    jsonNode.put(header, Integer.parseInt(value));
                }
                // Otherwise, treat it as a decimal
                else {
                    jsonNode.put(header, Double.parseDouble(value));
                }
            }
            // Otherwise, treat it as a string
            else {
                jsonNode.put(header, value);
            }
        }
        return jsonNode;
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private static boolean isValidDateFormat(String value) {
        Pattern dateFormat = Pattern.compile(DATE_FORMAT_PATTERN);
        Matcher matcher = dateFormat.matcher(value);
        return matcher.matches();
    }

    private static String formatDateString(String value) {
        LocalDate date = null;
        try {
            date = parseDate(value, DateTimePatternConstant.DATE_PATTERN);
        } catch (DateTimeParseException e) {
            try {
                date = parseDate(value, DateTimePatternConstant.DATETIME_PATTERN_1);
            } catch (DateTimeParseException e1) {
                try {
                    date = parseDate(value, DateTimePatternConstant.DATE_PATTERN_2);
                } catch (DateTimeParseException e2) {
                    log.error("Unable to parse the date string: {}", e.getMessage());
                }
            }
        }
        return date != null ? date.format(OUTPUT_DATE_FORMATTER) : value;
    }

    private static LocalDate parseDate(String dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(dateStr, formatter);
    }

    private static void writeJsonNode(FileWriter writer, ObjectNode jsonNode) {
        try {
            String jsonString = objectMapper.writeValueAsString(jsonNode);
            String formattedJson = jsonString.replace(":", ": ");
            writer.write(formattedJson + System.lineSeparator());
        } catch (IOException e) {
            log.error("Error while writing to file: {}", e.getMessage());
        }
    }
}

