# bookingserv

## Application Overview
bookingserv is a spring boot rest application which would provide the CRUD operations for `Booking` resource.

There are three modules in this application
- bookingservApi - This module contains the interface.
    - `v1/schema/booking.json` defines the booking resource.
    - `jsonschema2pojo-maven-plugin` is being used to create `Booking POJO` from json file.
    - `BookingResource.java` is the interface for CRUD operations on `Booking` resource.
        - POST `/v1/bfs/booking` endpoint defined to create the resource.
- bookingservImplementation - This module contains the implementation for the rest endpoints.
    - `BookingResourceImpl.java` implements the `BookingResource` interface.
- bookingservFunctionalTests - This module would have the functional tests.

## How to run the application
- Please have Maven version `3.3.3` & Java 8 on your system.
- Use command `mvn clean install` to build the project.
- Use command `mvn spring-boot:run` from `bookingservImplementation` folder to run the project.
- Use postman or curl to access `http://localhost:8080/v1/bfs/booking` POST or GET endpoint.

## Assignment
We would like you to enhance the existing project and see you complete the following requirements:

- `booking.json` has only `name`, and `id` elements. Please add `date of birth`, `checkin`, `checkout`, `totalprice`, `deposit` and `address` elements to the `Booking` resource. Address will have `line1`, `line2`, `city`, `state`, `country` and `zip_code` elements. `line2` is an optional element.
- Add one more operation in `BookingResource` to Get All the bookings. `BookingResource` will have two operations, one to create, and another to retrieve all bookings.
- Implement create and get all the bookings operations in `BookingResourceImpl.java`.
- Please add the unit tests to validate your implementation.
- Please use h2 in-memory database or any other in-memory database to persist the `Booking` resource. Dependency for h2 in-memory database is already added to the parent pom.
- Please make sure the validations done for the requests.
- Response codes are as per rest guidelines.
- Error handling in case of failures.
- Implement idempotency logic to avoid duplicate resource creation.

## Assignment submission
Thank you very much for your time to take this test. Please upload this complete solution in Github and send us the link to `bfs-sor-interview@paypal.com`.

## Submitted assignment information
- A new resource is created - `address.json` which is referenced inside `booking.json`. In future, if there are more fields to be added to address, only Address resource would be impacted and not the Booking entity.
- Create and Get All bookings operations are implemented.
  Create booking - 
  POST http://localhost:8080/v1/bfs/booking
  Response Codes : 
  Successful response code - 201 Accepted
  When there is duplicate booking - 400 BAD REQUEST with appropriate message
  When there is an overlapped booking - 400 BAD REQUEST with appropriate message
  When any of the required field is not provided - 400 BAD REQUEST with appropriate message
  Sample request body - 
    {
    "first_name":"Gurudayal",
    "last_name" : "Khosla",
    "date_of_birth": 512658610000,
    "checkin" : 1626527415,
    "checkout" : 1626527430,
    "total_price" : 21.0,
    "deposit" : 10.50,
    "address" : {
        "line1" : "line1",
        "city" : "Bangalore",
        "state" : "Karnataka",
        "country" : "India",
        "zip_code" : "560100"
    }
  }
  Sample response body -
  {
    "id":1,
    "first_name":"Gurudayal",
    "last_name" : "Khosla",
    "date_of_birth": 512658610000,
    "checkin" : 1626527415,
    "checkout" : 1626527430,
    "total_price" : 21.0,
    "deposit" : 10.50,
    "address" : {
        "line1" : "line1",
        "city" : "Bangalore",
        "state" : "Karnataka",
        "country" : "India",
        "zip_code" : "560100"
    }
  }
  
  Get booking -
  GET http://localhost:8080/v1/bfs/booking
  Response Codes : 
  Getting bookings successfully - 200 OK
  No content found - 204 No Content
  Sample response body - Array of booking json objects
  [
    {
        "id": 1,
        "first_name": "Gurudayal",
        "last_name": "Khosla",
        "date_of_birth": 512658610000,
        "checkin": 1626527415,
        "checkout": 1626527430,
        "total_price": 21.0,
        "deposit": 10.5,
        "address": {
            "line1": "line1",
            "city": "Bangalore",
            "state": "Karnataka",
            "country": "India",
            "zip_code": "560100"
        }
    }
  ]
  
- Total 16 major junit tests are added inside bookingservFunctionalTests module.
- Validations are implemented for required fields and duplicate bookings.
- Duplicate and overlapped bookings are not allowed
- i18n support is added inside com.paypal.bfs.test.bookingserv.i18n.MessageUtil. For messages in different locales, need to use Accept-Language header and message properties files for different locales.
- Controller adviser is implemented to handle the error messages at application level- com.paypal.bfs.test.bookingserv.controller.adviser.ApplicationExceptionHandlerAdvice
  
  
  
