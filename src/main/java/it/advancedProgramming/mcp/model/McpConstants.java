package it.advancedProgramming.mcp.model;

/**
 * MCP CSV constants class.
 * Provides constants for MCP serve.
 *
 * @author G.NASO
 * @version 1.0
 */
public class McpConstants {

    public static final String HELLO_TOOL_NAME = "hello";
    public static final String HELLO_TOOL_DES = "Send hello";
    public static final String LIST_TOOL_NAME = "csvList";
    public static final String LIST_TOOL_DES = "List CSV file in directory the default is ..\\ directory";
    public static final String OPEN_TOOL_NAME = "openCsv";
    public static final String OPEN_TOOL_DES = "Opens the CSV file and returns max the first 20 rows. " +
            "Possible parameters are: the required file path, an optional column separator (default is ,)," +
            "an optional boolean indicate if the columns name are in thee first line default is false " +
            "and  an optional lines number (max 20)";
    public static final String FILTER_TOOL_NAME = "filterCsv";
    public static final String FILTER_TOOL_DES = "You can apply one or more filters to the CSV. The possible comparison " +
            "operations are: EQUALS, NOT_EQUALS, GREATER_THAN, LESS_THAN, DATE_GREATER_THAN, and DATE_LESS_THAN.";

    public static final String DIR_PARM_DES = "Directory where to look for CSV files";
    public static final String PATH_PARM_DES = "Full path for CSV files";
    public static final String SEPARATOR_PARM_DES = "Columns separator";
    public static final String NAMES_IN_FIRST_LINE_DES = "Names on the first line, boolean value default is false";
    public static final String LINE_NUMBER_PARM_DES = "Number line max 20";
    public static final String FILTERS_PARM_DES = "List of filters of type FilterCondition, to be applied to the CSV";

    public static final String CSV = ".csv";
}
