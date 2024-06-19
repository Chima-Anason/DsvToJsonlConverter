import org.junit.Before;
import org.junit.Test;
import org.pst.ag.service.impl.ConversionService;
import org.pst.ag.service.impl.DsvToJsonlDsvToJsonlConverterImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;


public class DsvToJsonlDsvToJsonlConverterImplTest {

    private ConversionService conversionService;

    @Before
    public void setUp() {
        conversionService = new ConversionService( new DsvToJsonlDsvToJsonlConverterImpl());
    }

    @Test
    public void testConversion() throws IOException {
        String inputFile = "src/main/resources/files/DSV_input1.txt";
        String outputFile = "src/main/resources/files/output.json";
        char delimiter = ',';
        conversionService.executeConversion(inputFile, outputFile, delimiter);

        String expectedOutput = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("files/JSONL_output.jsonl").getPath())));
        String actualOutput = new String(Files.readAllBytes(Paths.get(outputFile)));

        assertEquals(expectedOutput, actualOutput);
        Files.delete(Paths.get(outputFile));
    }

    @Test
    public void testConversionWithDifferentDelimiter() throws IOException {
        String inputFile = "src/main/resources/files/DSV_input2.txt";
        String outputFile = "src/main/resources/files/output1.json";
        char delimiter = '|';
        conversionService.executeConversion(inputFile, outputFile, delimiter);
        String expectedOutput = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("files/JSONL_output.jsonl").getPath())));
        String actualOutput = new String(Files.readAllBytes(Paths.get(outputFile)));

        assertEquals(expectedOutput, actualOutput);
        Files.delete(Paths.get(outputFile));
    }
}