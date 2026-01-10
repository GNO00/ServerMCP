package it.advancedProgramming.mcp.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OpenCsvToolResponse {

    private String[] colunsName;
    private List<String[]> lines;
}
