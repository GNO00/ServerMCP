package it.advancedProgramming.mcp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * CSV model for condition.
 * Provide a condition structure to query CSV
 *  Fields:
 *  * - column: column name to filter
 *  * - operator: comparison operator
 *  * - value: value to compare
 *
 * @author G.NASO
 * @version 1.0
 */

@Getter
@Setter
@AllArgsConstructor
public class FilterCondition {

    private String column;
    private McpOperationEnum operator;
    private String value;
}
