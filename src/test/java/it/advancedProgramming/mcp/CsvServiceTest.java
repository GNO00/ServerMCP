package it.advancedProgramming.mcp;

import com.opencsv.exceptions.CsvException;
import it.advancedProgramming.mcp.csvService.CsvService;
import it.advancedProgramming.mcp.model.FilterCondition;
import it.advancedProgramming.mcp.model.McpOperationEnum;
import it.advancedProgramming.mcp.model.response.FilterCsvToolResponse;
import it.advancedProgramming.mcp.model.response.OpenCsvToolResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Test for CSV tools
 * A unit test class for tools. The purpose of this class is not to test the MCP server but simply to validate the correctness
 * of the implementation of CSV operations.
 *
 * @author G.NASO
 * @version 1.0
 */

@SpringBootTest
@ActiveProfiles("test")
public class CsvServiceTest {

    @Autowired
    private ResourcePatternResolver resolver;

    @Autowired
    private CsvService csvService;

    @Test
    public void helloTest() {
        Assertions.assertEquals("Hello from MCP Server!", csvService.helloWorld());
    }

    @Test
    public void listaCsvTest() throws IOException {
        Resource resource = resolver.getResource("classpath:testFile/");
        List<String> listFile = csvService.csvList(resource.getFile().getPath());
        Assertions.assertFalse(listFile.isEmpty());
        Assertions.assertEquals(3, listFile.size());
        Assertions.assertTrue(listFile.get(0).contains("separator"));
    }

    @Test
    public void openCsvTest() throws IOException, CsvException {
        Resource resource = resolver.getResource("classpath:testFile/testData.csv");
        OpenCsvToolResponse response = csvService.openCsv(resource.getFile().getPath(), null, true, 5);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getColunsName());
        Assertions.assertFalse(response.getLines().isEmpty());
        Assertions.assertEquals(5, response.getLines().size());
    }

    @ParameterizedTest
    @MethodSource("dataForFilterCsv")
    public void filterCsvTest(String fileName, List<FilterCondition> filters, String separator,
                              boolean nameInFirstLine, int resultLineNumber, String resultVal) throws IOException, CsvException {

        Resource resource = resolver.getResource("classpath:testFile/" + fileName);
        FilterCsvToolResponse response = csvService.filterCSv(resource.getFile().getPath(), filters, separator, nameInFirstLine);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(resultLineNumber, response.getFiltersLines().size());
        Assertions.assertEquals(resultVal, response.getFiltersLines().get(0).get(filters.get(0).getColumn()));
    }

    private static Stream<Arguments> dataForFilterCsv() {
        return Stream.of(
                //Simply test
                Arguments.of("testData.csv",
                        makeList(new FilterCondition("first_name", McpOperationEnum.NOT_EQUALS, "Elena")),
                        null, true, 9, "Marco"),

                Arguments.of("testData.csv",
                        makeList(new FilterCondition("first_name", McpOperationEnum.EQUALS, "Elena")),
                        null, true, 1, "Elena"),

                Arguments.of("testData.csv",
                        makeList(new FilterCondition("age", McpOperationEnum.LESS_THAN, "18")),
                        null, true, 1, "17"),

                Arguments.of("testData.csv",
                        makeList(new FilterCondition("birth_date", McpOperationEnum.DATE_GREATER_THAN, "1990-01-01")),
                        null, true, 7, "1990-03-12"),

                Arguments.of("testData.csv",
                        makeList(new FilterCondition("birth_date", McpOperationEnum.DATE_LESS_THAN, "1990-01-01")),
                        null, true, 3, "1983-01-08"),

                //No field name in first line test
                Arguments.of("studenti.csv",
                        makeList(new FilterCondition("4", McpOperationEnum.EQUALS, "Informatica")),
                        null, false, 5, "Informatica"),

                //Multiple filters test
                Arguments.of("testData.csv",
                        makeList(new FilterCondition("gender", McpOperationEnum.EQUALS, "F"),
                                 new FilterCondition("age", McpOperationEnum.GREATER_THAN, "18")),
                        null, true, 4, "F"),

                //Custom separator (;) test
                Arguments.of("separator.csv",
                        makeList(new FilterCondition("categoria", McpOperationEnum.EQUALS, "Ferramenta")),
                        ";", true, 3, "Ferramenta"));
    }

    private static List<FilterCondition> makeList(FilterCondition... filter) {
        return new ArrayList<>(Arrays.asList(filter));
    }
}

