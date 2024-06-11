# dotPaymentSystem

# Money Transfer Simulation Web Application

This project simulates a money transfer operation between two bank accounts, inspired by the NIBSS Instant Payments (NIP) system. The application is built using Java 21 and Spring Boot, and it includes REST endpoints for processing transfer requests, retrieving transaction lists, and generating transaction summaries. Additionally, the application features scheduled operations for commission calculations and daily transaction summaries.

## Features

1. **REST Endpoints**
    - **Outflow Transfer Request**: Accept and process outflow transfer requests to external bank accounts.
    - **Inflow Transfer Request**: Accept and process inflow transfer requests to internal accounts.
    - **Retrieve Transactions**: Retrieve a list of transactions with optional parameters such as status, accountId, and date range.
    
2. **Scheduled Operations**
    - **Commission Calculation**: Analyzes each transaction daily and updates successful ones as commission-worthy, calculating the commission value (20% of the transaction fee, with a transaction fee of 0.5% of the original amount, capped at 100).
    - **Daily Transaction Summary**: Produces a summary of transactions for a specified day.

## Technologies Used

- Java 21
- Spring Boot 3.2.6
- H2 Database
- OpenCSV (for CSV file generation)
- Apache Maven 3.8.3

## Setup and Execution
- Postman API documentation has been included in the project resource folder
  
### Running the Application

1. **Clone the Repository**

   ```bash
   git clone https://github.com/u-aikay/dotPaymentSystem.git
   cd payment-project
