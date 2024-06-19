package org.pst.ag;

import org.pst.ag.service.impl.ConversionService;
import org.pst.ag.service.DsvToJsonlConverter;
import org.pst.ag.service.impl.DsvToJsonlDsvToJsonlConverterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final String USAGE_MESSAGE = "Usage: java -jar DsvToJsonlConverter-1.0-SNAPSHOT.jar <inputFilePath> <outputFilePath> <delimiter>";

    public static void main(String[] args) {
        if (args.length < 3) {
            log.error(USAGE_MESSAGE);
            System.exit(1);
        }

        String inputFile = args[0];
        String outputFile = args[1];
        char delimiter = args[2].charAt(0);

        try {
            DsvToJsonlConverter dsvToJsonlConverter = new DsvToJsonlDsvToJsonlConverterImpl();
            ConversionService context = new ConversionService(dsvToJsonlConverter);

            long startTime = System.currentTimeMillis();
            context.executeConversion(inputFile, outputFile, delimiter);
            long endTime = System.currentTimeMillis();
            long timeTaken = endTime - startTime;

            log.info("File conversion completed successfully.");
            log.info("Time taken: {} seconds", timeTaken / 1000.0);

        } catch (Exception e) {
            log.error("An error occurred during file conversion: ", e);
        }
    }
}