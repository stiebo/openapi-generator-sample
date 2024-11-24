# SpringBoot API generated with OpenAPI Generator Maven Plugin

## Code generation

OpenAPI definition is under resources/openapi/antifraudsystem.yaml

Run

> mvn

and you'll fine the code under:

* target/generated-sources/openapi/index.adoc => Generated AsciiDoc
* target/generated-sources/openapi/src/main/java/dev/stiebo/openapi_generator_sample => Generated Java Code using delegate pattern

Run

> mvn spring-boot:start 

and you'll find Swagger under:

* [http://localhost:28859/swagger-ui.html](http://localhost:28859/swagger-ui.html)


## Background

Frauds carry significant financial costs and risks for all stakeholders. So, the presence of an anti-fraud system is a necessity for any serious e-commerce platform.

The Anti-Fraud System project provides a comprehensive framework for detecting and preventing fraudulent financial transactions. By integrating role-based access control, RESTful APIs, heuristic validation rules, and adaptive feedback mechanisms, the system offers a robust solution for financial institutions to safeguard against fraud. Leveraging Spring Boot and its associated technologies, the project demonstrates best practices in building secure, scalable, and maintainable applications in the financial sector.

Link to Github repository: [https://github.com/stiebo/Anti-Fraud-System](https://github.com/stiebo/Anti-Fraud-System)

Check out my Github profile: [https://github.com/stiebo](https://github.com/stiebo)

Link to the learning project: [https://hyperskill.org/projects/232](https://hyperskill.org/projects/232)

Check out my learning profile: [https://hyperskill.org/profile/500961738](https://hyperskill.org/profile/500961738)

## Key Components of the Anti-Fraud System

1. **Role-Based Access Control**:

    - **User Roles**: The system defines specific roles, including **Administrator**, **Merchant**, and **Support**.

    - **Permissions**:

        - **Administrator**: Manages user roles and access rights.

        - **Merchant**: Submits transactions for validation.

        - **Support**: Reviews and provides feedback on transactions.

    - This structure ensures that users have access only to functionalities pertinent to their roles, enhancing security and operational efficiency.


2. **RESTful API Endpoints**:

    - The system offers a set of REST endpoints for user interactions and transaction management:

        - **User Management**: Endpoints for registering users, assigning roles, and managing access.

        - **Transaction Processing**: Endpoints for submitting transactions and retrieving their statuses.

        - **Feedback Mechanism**: Allows fraud analysts to provide feedback on transaction validations.

    - These endpoints facilitate seamless communication between clients and the server, adhering to REST principles.


3. **Transaction Validation with Heuristic Rules**:

    - The system employs heuristic rules to assess transactions:

        - **Amount-Based Validation**: Transactions are categorized as ALLOWED, MANUAL_PROCESSING, or PROHIBITED based on their amounts.

        - **IP and Card Monitoring**: Identifies and blocks transactions from suspicious IP addresses or using stolen card numbers.

        - **Regional Analysis**: Evaluates transactions based on geographic regions to detect anomalies.

    - These rules help in identifying potentially fraudulent activities by analyzing transaction patterns and attributes.


4. **Feedback Mechanism**:

    - Support users (Fraud analysts) can provide feedback on transaction validations, indicating whether a transaction was correctly categorized.

    - The system adjusts its heuristic thresholds based on this feedback, improving its accuracy over time.

    - This adaptive approach ensures the system evolves with changing fraud patterns and reduces false positives or negatives.


5. **Authentication and Authorization**:

    - Utilizes Spring Security to implement authentication and authorization mechanisms.

    - Ensures that only authenticated users can access the system, with permissions tailored to their roles.

    - This setup protects sensitive operations and data from unauthorized access.


6. **Data Persistence**:

    - Employs Spring Data JPA for database interactions, managing user information, transaction records, and feedback data.

    - Ensures data integrity and supports efficient querying and storage operations.

## Tests
Integration tests were performed as part of the Hyperskill project with 150+ tests passed. See https://hyperskill.org/projects/232

## Users, Roles and Authorization

Our service supports the following roles:

| Endpoint / Role                               | Anonymous | MERCHANT | ADMINISTRATOR | SUPPORT |
|-----------------------------------------------|-----------|----------|---------------|---------|
| POST /api/antifraud/transaction               | -         | +        | -             | -       |
| POST /api/auth/user                           | +         | +        | +             | +       |
| GET /api/auth/list                            | -         | -        | +             | +       |
| DELETE /api/auth/user                         | -         | -        | +             | -       |
| PUT /api/auth/access                          | -         | -        | +             | -       |
| PUT /api/auth/role                            | -         | -        | +             | -       |
| POST, DELETE, GET api/antifraud/suspicious-ip | -         | -        | -             | +       |
| POST, DELETE, GET api/antifraud/stolencard    | -         | -        | -             | +       |
| GET /api/antifraud/history                    | -         | -        | -             | +       |
| PUT /api/antifraud/transaction                | -         | -        | -             | +       |

The service requires Http Basic authentication for all endpoints except for user signup.
Users can sign up themselves via *POST /api/auth/user*. The Administrator is the user who registered first, all subsequent registrations automatically receive the MERCHANT role and their account is locked by default. Users can be unlocked and roles changed by the Administrator (see below).

## Transaction validation
As the central entry point to the API, transactions can be posted by customers (merchants).

Transactions are checked based on
1. Transaction amount:
- Transactions with a sum of lower or equal to <allowed-threshold> are ALLOWED (default threshold: 200)
- Transactions with a sum of greater than 200 but lower or equal than <manual_processing-threshold> require MANUAL_PROCESSING (default threshold: 1500)
- Transactions with a sum of greater than <manual_processing-threshold> are PROHIBITED.
2. Stolen cards (checked also using the Luhn algorithm) are PROHIBITED
3. Suspicious IP Addresses (must be valid IPv4 address) are PROHIBITED
4. unique regions and IP addresses (correlation):
- Transaction is PROHIBITED if there are transactions with the same number within the last hour from more than 2 regions or more than 2 unique IP addresses of the world other than the region or IP address of the transaction that is currently being verified;
- Transaction is sent for MANUAL_PROCESSING if there are transactions with the same number within the last hour from 2 regions or 2 unique IP addresses of the world other than the region or IP address of the transaction that is currently being verified;

Possible regions (Code, Description):
- EAP	East Asia and Pacific
- ECA	Europe and Central Asia
- HIC	High-Income countries
- LAC	Latin America and the Caribbean
- MENA  The Middle East and North Africa
- SA	South Asia
- SSA	Sub-Saharan Africa

## Threshold adjustment
Fraud analysts provide feedback on transaction validations, indicating whether a transaction was correctly categorized.
The system then adjusts its heuristic thresholds based on this feedback, improving its accuracy over time:

| Transaction Validity / Feedback | ALLOWED                 | MANUAL_PROCESSING                  | PROHIBITED                    |
|---------------------------------|-------------------------|------------------------------------|-------------------------------|
| ALLOWED                         | exception               | decrease `<allowed-threshold>`     | decrease `<allowed-threshold>` |
| MANUAL_PROCESSING               | increase `<allowed-threshold>` | exception                        | decrease `<manual-threshold>`  |
| PROHIBITED                      | increase `<allowed-threshold>` | increase `<manual-threshold>`    | exception                      |
