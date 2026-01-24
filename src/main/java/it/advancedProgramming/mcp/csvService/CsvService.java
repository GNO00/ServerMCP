package it.advancedProgramming.mcp.csvService;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import it.advancedProgramming.mcp.model.FilterCondition;
import it.advancedProgramming.mcp.model.McpConstants;
import it.advancedProgramming.mcp.model.response.FilterCsvToolResponse;
import it.advancedProgramming.mcp.model.response.OpenCsvToolResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * MCP CSV service for Spring AI integration.
 * Provides tools for CSV query.
 *
 * @author G.NASO
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CsvService {

    // Inject CsvFilterService bean
    private final CsvFilterService csvFilterService;

    // Default path defined in application.yml
    @Value("${default.path.csv.directory}")
    private String defaultPath;

    /**
     * TOOL Simple check to make sure the server is running
     *
     */
    @Tool(name = McpConstants.HELLO_TOOL_NAME, description = McpConstants.HELLO_TOOL_DES)
    public String helloWorld() {
        log.info("Start toll {}",  McpConstants.HELLO_TOOL_NAME);
        return "Hello from MCP Server!";
    }

    /**
     * TOOL List CSV file in directory
     *
     * @param path The path where the CSV files are downloaded; this parameter is not required. The default is defined as an
     *             attribute in the application.yml file.
     * @return Path list of found CSV files
     */
    @Tool(name = McpConstants.LIST_TOOL_NAME, description = McpConstants.LIST_TOOL_DES)
    public List<String> csvList(@McpToolParam(description = McpConstants.DIR_PARM_DES, required = false) String path) throws IOException {
        log.info("Start toll {}",  McpConstants.LIST_TOOL_NAME);
        try (var stream = Files.list(Path.of(path == null ? defaultPath : path))) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(McpConstants.CSV))
                    .map(p -> p.getFileName().toString())
                    .toList();
        }
    }

    /**
     * TOOL Open CSV open the file and return the first lines (max 20)
     *
     * @param path The full CSV path
     * @param separator Columns separator default is comma
     * @param nameInFirstLine boolean value indicating whether the CSV file has field names in the first row default is false
     * @param number Number of lines retuned (max 20)
     *
     * @return OpenCsvToolResponse object
     */
    @Tool(name = McpConstants.OPEN_TOOL_NAME, description = McpConstants.OPEN_TOOL_DES)
    public OpenCsvToolResponse openCsv(@McpToolParam(description = McpConstants.PATH_PARM_DES) String path,
                                       @McpToolParam(description = McpConstants.SEPARATOR_PARM_DES, required = false) String separator,
                                       @McpToolParam(description = McpConstants.NAMES_IN_FIRST_LINE_DES, required = false) boolean nameInFirstLine,
                                       @McpToolParam(description = McpConstants.LINE_NUMBER_PARM_DES, required = false) Integer number) throws IOException, CsvException {
        log.info("Start toll {}",  McpConstants.OPEN_TOOL_NAME);
        CSVReader reader = makeCsvReader(path, separator);
        if(number == null || number > 20) number = 20;

        Iterator<String[]> iterator = reader.iterator();
        OpenCsvToolResponse  response = new OpenCsvToolResponse();
        if(nameInFirstLine && iterator.hasNext()) response.setColunsName(iterator.next());
        List<String[]> result = new ArrayList<>();
        while (iterator.hasNext() && number > 0) {
            result.add(iterator.next());
            number--;
        }
        reader.close();
        log.info("Return {} lines in CSV files {}", result.size(), path);
        response.setLines(result);
        return response;
    }

    /**
     * TOOL Filter CSV filter a selected CSV file
     *
     * @param path The full CSV path
     * @param filters It is a list of FilterCondition objects, providing the structure for managing filters.
     *                Multiple filters can be applied in the same request.
     * @param separator Columns separator default is comma
     * @param nameInFirstLine boolean value indicating whether the CSV file has field names in the first row default is false
     *
     * @return FilterCsvToolResponse
     */
    @Tool(name = McpConstants.FILTER_TOOL_NAME, description = McpConstants.FILTER_TOOL_DES)
    public FilterCsvToolResponse filterCSv(@McpToolParam(description = McpConstants.PATH_PARM_DES) String path,
                                           @McpToolParam(description = McpConstants.FILTERS_PARM_DES) List<FilterCondition> filters,
                                           @McpToolParam(description = McpConstants.SEPARATOR_PARM_DES, required = false) String separator,
                                           @McpToolParam(description = McpConstants.NAMES_IN_FIRST_LINE_DES, required = false) boolean nameInFirstLine) throws IOException, CsvValidationException {
        log.info("Start toll {}",  McpConstants.FILTER_TOOL_NAME);
        CSVReader reader = makeCsvReader(path, separator);
        return csvFilterService.filterCsv(reader, nameInFirstLine, filters);
    }


    /**
     * Private method makeCsvReader for prepare a CSVReader object
     *
     * @param path The full CSV path
     * @param separator Columns separator default is comma
     *
     * @return CSVReader object
     */
    private CSVReader makeCsvReader(String path, String separator) throws FileNotFoundException {

        if(!path.toLowerCase().endsWith(".csv")) throw new FileNotFoundException(path);
        CSVReaderBuilder bulder = new CSVReaderBuilder(new FileReader(path));
        if (separator != null && !separator.isEmpty()) {
            bulder.withCSVParser(new CSVParserBuilder().withSeparator(separator.charAt(0)).build());
        }
        return bulder.build();
    }
}
