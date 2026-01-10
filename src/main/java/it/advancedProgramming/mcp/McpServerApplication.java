package it.advancedProgramming.mcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application,
 * This class defines the main method that will start the Spring Boot application.
 * Notice that the class is annotated with @SpringBootApplication. The stereotype annotation contains @ComponentScan, which will
 * search for the defined Beans within the package.
 *
 * @author G.NASO
 * @version 1.0
 */
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
}
