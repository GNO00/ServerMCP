package it.advancedProgramming.mcp.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FilterCsvToolResponse {

    private List<Map<String, String>> filtersLines;
}
