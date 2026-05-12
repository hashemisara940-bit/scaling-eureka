# Enterprise SOAP Testing Desktop Application

## Architecture
- Clean architecture style with `domain`, `infrastructure`, `shared`, `ui`, and `application` modules.
- JavaFX + Spring Boot integration for desktop app lifecycle.
- SOAP execution via Spring-WS `WebServiceTemplate`.
- SQLite default persistence through Spring Data JPA.

## Project Structure
- `application`: bootstrap and runtime startup.
- `domain`: entities, service contracts, repository contracts.
- `infrastructure`: SOAP client implementation, config, reporting/load-test adapters.
- `ui`: JavaFX controllers and MVVM-compatible view wiring.
- `shared`: DTOs, mappers, exceptions.

## Delivered Enterprise Modules (scaffold + extension points)
1. SOAP service engine (`SoapExecutionService`).
2. WSDL parser module (planned adapter in infrastructure).
3. Variable engine (planned domain service + resolver chain).
4. Embedded load testing engine (planned JMeter adapter).
5. Reporting/export module (planned adapters for PDF/CSV/HTML/DOCX).
6. Database schema via JPA entities (`soap_requests`).

## Build and Run
```bash
mvn clean package
mvn javafx:run
```

## Implementation Plan (next iterations)
1. Add XML syntax-highlighting editor component (RichTextFX).
2. Implement WSDL import tree and request generator.
3. Add env/variable inheritance and secure secret storage.
4. Build JMeter-backed concurrent load testing engine and live charts.
5. Add reporting adapters (OpenPDF, Apache POI, Thymeleaf HTML export).
6. Package installers using `jpackage` for Windows/macOS/Linux.
