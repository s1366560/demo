# Project Documentation

## Project Structure

```
calculation-core Core business logic
calculation-api External interfaces
calculation-domain Business object definitions
calculation-e2e Integration tests
scripts Load testing scripts, elastic scaling test scripts
```

## Architecture Diagram
```mermaid
graph TB
    subgraph "API Service Layer"
        A1[User API]
        A2[Account API]
        A3[Transaction API]
        A4[Balance API]
    end

    subgraph "Service Layer"
        B1[UserService]
        B2[AccountService]
        B3[TransactionService]
        B4[BalanceService]
    end

    subgraph "Data Access Layer"
        C1[UserDAO]
        C2[AccountDAO]
        C3[TransactionDAO]
        C4[BalanceDAO]
    end

    subgraph "Cache Layer"
        D1[User Cache]
        D2[Account Cache]
        D3[Transaction Cache]
        D4[Balance Cache]
    end

    subgraph "Storage Layer"
        E1[(MySQL Master)]
        E2[(MySQL Slave)]
        E3[(Redis Cluster)]
    end

    %% API to Service calls
    A1 --> B1
    A2 --> B2
    A3 --> B3
    A4 --> B4

    %% Service to DAO calls
    B1 --> C1
    B2 --> C2
    B3 --> C3
    B4 --> C4

    %% DAO to Storage Layer access
    C1 & C2 & C3 & C4 --> E1
    E1 --> E2

    %% Cache access relationships
    B1 --> D1
    B2 --> D2
    B3 --> D3
    B4 --> D4
    D1 & D2 & D3 & D4 --> E3

```

## Quick Start

- Start Redis
```
docker-compose service redis up -d
```
- Start MySQL
```
docker-compose service mysql up -d
```
- Start the project
```
mvn spring-boot:run
```
- Run performance tests

If running locally, modify the test plan to adjust the API endpoints to local addresses

```
jmeter -n -t scripts/load-test-plan.jmx -l scripts/load-test-result.jtl
```

- Run unit tests
```
mvn test
```

- Helm installation
```
helm install calculation-core ./charts/calculation-core
```

- Use [testkube](https://testkube.io/blog/jmeter-and-kubernetes-how-to-run-tests-efficiently-with-testkube) to run load tests

```
kubeclt apply -f scripts/load-test.yaml
testkube run testworkflow jmeter
```

# Code Coverage Report

![alt text](image.png)

# Horizontal Scaling Test Report

To make it easier to trigger horizontal scaling conditions, CPU threshold is adjusted to 8%;

![alt text](autoscaling1.png)

After running load test to trigger scaling:

![alt text](autoscaling2.png)


# Performance Test Report

## Test Resources

CPU: 500m
Memory: 1280Mi

## Test Results

![alt text](jmeter1.png)
![alt text](jmeter2.png)


