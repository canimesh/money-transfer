# Overview

This project provides an implementation for the following API
- account creation
- account retrieval 
- money transfer between accounts
- retrieve money transfer details

It uses the concept of [Event Sourcing](https://martinfowler.com/eaaDev/EventSourcing.html), [Command Query Responsibility Segregation](https://martinfowler.com/bliki/CQRS.html), [Eventbus](https://dzone.com/articles/design-patterns-event-bus) and [Domain Driven Design](https://en.wikipedia.org/wiki/Domain-driven_design) as part of the implementation.

Please note, this is my first attempt for these concepts. I am pretty sure that a lot can be done better in this code base.


To launch the application, please locate the class 
`com.canimesh.revolut.assignment.MoneyTransferApplication` and launch it.

Alternatively, you can use Gradle to build the project and then run the same.
`gradlew clean build -x test && java -jar build/libs/revolut-money-transfer.jar`

Please note you can run the test which include the API tests to show various usecases after running the application by running the following in a separate command window/prompt
`gradle test`


The application would be running on the following address 
`http://localhost:8888`

You can also view the swagger ui by going to the following address
`http://localhost:8888/swagger-ui`
You can also also perform tests using the swagger UI as well



# API details

The application exposes 4 API endpoints
- account creation
- account retrieval 
- money transfer between accounts
- retrieve money transfer details


## Account creation

To create an account execute an 
HTTP POST to 
`http://localhost:8888/accounts` with a body like the following

```$json
{
   "owner": "Animesh",
   "amount": 500
}
```

Where 
- *owner* is the name of the account owner
- *balanceCents* is the account balance in cents (optional, if missing 0 will be assumed)


The *owner* field is mandatory. Failure to specify it will cause the API to return HTTP status 400.

If successful, the API will return an HTTP 201 - created with `location` header as part of the response
`http://localhost:8888/accounts/62dae1d4-ce4d-46af-b2d5-885417408095` 

## Account retrieval

Once an account has been created, it can retrieved executing an HHTP GET to the following URL

`http://localhost:8888/accounts/<account-id>`

For instance, to retrieve the information of the account created above, the URL will be

`http://localhost:8888/accounts/62dae1d4-ce4d-46af-b2d5-885417408095`

If successful, the API will return an HTTP 200 with a body like the following
```$json
{"id":"62dae1d4-ce4d-46af-b2d5-885417408095","owner":"Animesh","amount":500}
``` 

## Transfer request

To execute a money transfer execute an HTTP POST to

`http://localhost:8888/transfers`

with a body like the example

```
{
   	"fromAccount": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
   	"toAccount": "3fa85f64-5717-4562-b3fc-2c963f66abcd",
   	"amount": 100
}
```

All the fields are mandatory.

A successful transfer will return a HTTP 201 created with a `location` header pointing to the money transfer resource.

`http://localhost:8888/transfers/62dae1d4-ce4d-46af-b2d5-885417408095`

## Retrieve money transfer details

The same transfer details can be retrieved later with a HTTP GET at

`http://localhost:8888/transfers/<transfer+id>`


The response would look like the following

```$json
{"id":"fed048a8-aec7-4431-8a69-7853eb661161","fromAccount":"ba60b35b-6675-4dbc-a33f-df717041b702","toAccount":"c3e0a5df-d746-4787-b3c4-fc7cfc72917a","amount":29,"statusChanges":[{"time":"2019-11-29T22:29:35.645Z","status":"INITIAL"},{"time":"2019-11-29T22:29:35.654Z","status":"DEBITED"},{"time":"2019-11-29T22:29:35.656Z","status":"CREDITED"}]}

```

The response returns you the id, from account id, to account id, amount and the list of the status changes which reflect the status of the transaction at different time stamps and their respective statuses. For now, the statuses can be one or more of the following: `INITIAL`, `DEBITED`, `CREDITED`, `FAILED`. 

# Tests

Execute `gradle test` to launch the tests. These test both the Java code and the API endpoints.

# TODO
- Write unit tests for almost all the classes - have not written those for brevity. Please note, there is one UT written using mockito to show the intent.
- Integratw with DI tools like Dagger2 etc
- Separate transfer details read as part of query responsibility segregation
- Add version and timestamp comparison for aggregates events handling especially for a distributed event sourcing system which might use a MQ and several listener systems
- Clean up code 
- Improve DDD and introduce more encapsulation