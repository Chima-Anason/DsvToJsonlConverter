package org.pst.ag.service.impl;

import org.pst.ag.service.DsvToJsonlConverter;

import java.io.IOException;

public class ConversionService {
    private DsvToJsonlConverter dsvToJsonlConverter;

    public ConversionService(DsvToJsonlConverter dsvToJsonlConverter) {
        this.dsvToJsonlConverter = dsvToJsonlConverter;
    }

    public void executeConversion(String inputFile, String outputFile, char delimiter) throws IOException {
        dsvToJsonlConverter.convert(inputFile, outputFile, delimiter);
    }

}
