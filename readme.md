# Skip The Dishes

<!-- TOC -->

* [Skip The Dishes](#skip-the-dishes)
    * [Problem Definition](#problem-definition)
    * [Requirements](#requirements)
    * [Execution](#execution)
    * [Data Model](#data-model)
        * [Test Coverage](#test-coverage)
    * [Demo](#demo)
    * [Improvements](#improvements)

<!-- TOC -->

## Problem Definition

Develop a project in Java that subscribes to some events, calculates the courier
statements and provides some REST endpoints. The following requirements should be
supported.

* The project should subscribe to the following events (DeliveryCreated,
  AdjustmentModified, BonusModified) and payloads.
    * DeliveryCreated
        * deliveryId
        * courierId
        * createdTimestamp
        * value
    * AdjustmentModified
        + adjustmentId

        * deliveryId
        * modifiedTimestamp
        * value
    * BonusModified
        * bonusId
        * deliveryId
        * modifiedTimestamp
        * value
    * Create a REST endpoint to return delivery transactions by period and courier.
        * A delivery transaction is the sum of all values (delivery, adjustments and
          bonuses) for a given deliveryId.
    * Create a REST endpoint to return the weekly courier statement for a specific courier.
        * The weekly courier statement is the sum of all delivery transactions.
        * The weekly courier statement is associated with a specific courier.

## Requirements

This project is compiling with Java 11, uses Maven 3.6.3. for packaging, Rabbitmq 3.5.3, docker 20.10.14 and docker compose 2.4.1

Docker and Docker compose are optional, since, they are used to start up a Rabbitmq instance and connect it with the app and help with the development.

## Execution

In order to execute the project there are to options described below:

* Provide a Rabbitmq instance, update the connection details in the [application.yml](src%2Fmain%2Fresources%2Fapplication.yml), and compile and execute the Spring boot fat jar
  generated.
* Alternatively, you can run script [start.sh](start.sh), This execute all steps required to run the application, below are described the steps:
    * Compile the spring boot application using 'mvn clean spring-boot:run'.
    * Execute a docker compose file [docker-compose.yml](docker-compose.yml)
        * Create a docker container with the spring boot
        * Create a docker container with Rabbitmq

## Data Model

In order to store the different events received by the application a separate tables was created,
and then database view was created to help to present the data needed in the REST endpoints.
The below diagram shows how the tables are organized.

![database_model.png](img%2Fdatabase_model.png)

### Test Coverage

The project include a test suite to include unit test and RestAPI test and event listener test.
The sample report generated with IntelLiJ EDA is shown below:

![coverage.png](img%2Fcoverage.png)

## Demo

For convenience a script file is provide to execute the application as below:

```console
$  ./start.sh
```

![start_output.png](img%2Fstart_output.png)
By default, the application runs on port '8080' and expose a swagger ui in the url

[swagger-ui](http://localhost:8080/swagger-ui/index.html)
![swagger_ui.png](img%2Fswagger_ui.png)

The H2 console is expose the below url

http://localhost:8080/h2

![h2_console.png](img%2Fh2_console.png)

The Rabbitmq console is expose the below url, and there topics(exchange) are created automatically by spring boot.

http://localhost:15670/#

![rabbitmq_console.png](img%2Frabbitmq_console.png)

When a event is successfully published in the topic, the application produce a log as It is shown below:
![publishing_event.png](img%2Fpublishing_event.png)
![publishing_event_console.png](img%2Fpublishing_event_console.png)

## Improvements

* This project uses the default error handling provided by spring boot, however It should enhancement to present a better error details.
* The test coverage can be increased.
* The current test suit covers some of the element of the integration test, but, It needs to be improved.





