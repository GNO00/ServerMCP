package it.advancedProgramming.mcp.csvService;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import it.advancedProgramming.mcp.model.FilterCondition;
import it.advancedProgramming.mcp.model.response.FilterCsvToolResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

/**
 * MCP CSV filter service for apply filters.
 *
 * @author G.NASO
 * @version 1.0
 */
@Slf4j
@Service
public class CsvFilterService {

    public FilterCsvToolResponse filterCsv(CSVReader reader, boolean nameInFirstLine, List<FilterCondition> filters) throws CsvValidationException, IOException {
        log.info("Start filtering csv service");
        FilterCsvToolResponse response = new FilterCsvToolResponse();
        String[] headers = null;
        Iterator<String[]> iterator = reader.iterator();

        if (nameInFirstLine && iterator.hasNext()) {
            log.info("Columns name in first line");
            headers = iterator.next();
        } else if (!iterator.hasNext()) {
            log.error("The CSV file is empty");
            return response;
        }

        String[] row;
        response.setFiltersLines(new ArrayList<>());
        log.info("Start apply filters");
        while (iterator.hasNext()) {
            if (headers == null) {
                row = iterator.next();
                headers = IntStream.rangeClosed(1, row.length)
                        .mapToObj(String::valueOf)
                        .toArray(String[]::new);
            }else{
                row = iterator.next();
            }
            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < headers.length; i++)
                map.put(headers[i], row[i]);

            if (matchesFilters(map, filters))
                response.getFiltersLines().add(map);
        }
        log.info("Number of lines found after applying filters {}", response.getFiltersLines().size());
        return response;
    }

    private boolean matchesFilters(Map<String, String> row, List<FilterCondition> filters) {
        for (FilterCondition filter : filters) {
            String cellValue = row.get(filter.getColumn());
            if (!compare(cellValue, filter))
                return false;
        }
        return true;
    }

    private boolean compare(String cellValue, FilterCondition filter) {
        String filterValue = filter.getValue();
        try {
            return switch (filter.getOperator()) {
                case EQUALS -> cellValue.equals(filterValue);
                case NOT_EQUALS -> !cellValue.equals(filterValue);
                case GREATER_THAN -> Double.parseDouble(cellValue) > Double.parseDouble(filterValue);
                case LESS_THAN -> Double.parseDouble(cellValue) < Double.parseDouble(filterValue);
                case DATE_GREATER_THAN -> LocalDate.parse(cellValue).isAfter(LocalDate.parse(filterValue));
                case DATE_LESS_THAN -> LocalDate.parse(cellValue).isBefore(LocalDate.parse(filterValue));
            };
        }catch (Exception e){
            log.error("Filter operator {} error", filter.getOperator());
            log.error(e.getMessage());
            return false;
        }
    }
}
