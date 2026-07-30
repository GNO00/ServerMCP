# ServerMCP

This repository contains the server-side implementation of the MCP project, developed in **Java** and managed with **Maven**.

## 🚀 Overview
ServerMCP is a modular Java-based server designed to support the MCP project architecture.  
It provides the core backend logic, communication handling, and extensible components required for the system.
Technical documentation and sample CSV files under the documentation folder

## 📁 Project Structure
ServerMCP/  
│  
├── src/  
│   ├── main/  
│   │   ├── java/  
│   │   └── resources/  
│   └── test/  
│  
├── pom.xml  
└── .gitignore  

## 🛠️ Technologies Used
- Java 25
- Maven 3
- Spring Boot 3.5.8
- JUnit 

## 📦 Installation
git clone https://github.com/GNO00/ServerMCP.git
cd ServerMCP
mvn clean install

## ▶️ Running the Server
Inside the jar folder there is the fat jar with all the dependencies:

java -jar ServerMCP-0.0.1.jar

## ⚙️ Configuration
If not specified as a parameter, the default path for the csv files is the one from where the jar is launched.
The server is exposed on port 8080 and the URL is http://localhost:8080/mcp
If you're using MCP-Inspector for testing, connect using the built-in proxy.

## 🧪 Testing
mvn test

## 👤 Author
**GNO00**  
GitHub: https://github.com/GNO00
