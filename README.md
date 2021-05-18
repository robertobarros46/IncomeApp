## Income API
### Technologies used

* Java OpenJDK 11
* Spring
* Docker
* Maven
* Swagger
* Loombok

### Description

This API is responsible to manage transactions on a respective users account.
User can make transactions on it account by following the next steps, also user can get
their current balance.

### Running
1. Make sure that you have installed all the necessary tools.
   If you are running with docker.
    * Docker
    * Maven

   To run locally without docker you will need to install openJDK 11

         openjdk version "11.0.2" 2019-01-15

2. To run the project:

        mvn clean package

3. To run the project without tests:

        mvn clean package -PskipTests

4. It will clean, compile and generate a jar at target dir. After that just run the docker-compose command:

        docker build -t incomeapp .

This will create a docker image, after this you just need to run the following command.

        docker run -p 8080:8080 incomeapp

### How to use

To check the endpoints available in this application, you can run it, following the steps above and access:

      localhost:8080/swagger-ui.html/

This will display a swagger documentation with the available endpoints and their required bodies.

**Make transaction**

      {
         "accountNumber": "123",
         "amount": 20.0,
         "transactionType": "ADD"
      }

**Get current balance**

       {
          "accountNumber": "123"
       }

Remember that the application has some validation, so if the user tries to
remove money from their account and he or she does not have limit, a response will be
displayed such this one:

      {
         "status": "BAD_REQUEST",
         "validationErrors": [
            "insufficient-limit"
         ]
      }

If the user tries to get balance from an account that doesn't exists an error such as this one
will be in the response:

      {
         "status": "NOT_FOUND",
         "errorMessage": "Account not found"
      }

Also, if the user tries to remove money from a account that does not exists an error such as this
will be displayed:

      {
         "status": "BAD_REQUEST",
         "validationErrors": [
            "account-does-not-exists"
         ]
      }

### Test

To run the tests, unit and integration:

    mvn test