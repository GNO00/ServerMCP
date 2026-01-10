package it.advancedProgramming.mcp.config;

import it.advancedProgramming.mcp.csvService.CsvService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MCP Server bean Configuration class.
 *
 * @author G.NASO
 * @version 1.0
 */
@Configuration
public class BeanConfiguration {

    /**
     * Registers MCP tools from the provided service with the MCP server.
     *
     * This bean creates a ToolCallbackProvider that automatically discovers and exposes
     * all @Tool-annotated methods from CsvService.
     * Without explicit registration, Spring AI auto-configuration handles @Service
     * scanning, but this provides precise control for multiple services or custom logic.
     *
     * @param csvService the service containing MCP tools
     * @return configured ToolCallbackProvider for MCP protocol
     */
    @Bean
    public ToolCallbackProvider toolProvider(CsvService csvService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(csvService)  // Scan for @Tool methods
                .build();
    }

}
