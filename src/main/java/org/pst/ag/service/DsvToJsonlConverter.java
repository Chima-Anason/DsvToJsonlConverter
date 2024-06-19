package org.pst.ag.service;

import java.io.IOException;

public interface DsvToJsonlConverter {

    void convert(String inputFile, String outputFile, char delimiter) throws IOException;
}