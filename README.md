# eGym Recruiting Coding Task

**Do not share this project or its solution with people outside of eGym GmbH!**

## Technologies used in the project

* Oracle/Open JDK 1.7 or 1.8 (must be pre-installed).
* [Gradle](http://gradle.org/) for build automation.
* [Guice](https://github.com/google/guice) for dependency injection.
* [JPA/Hibernate](http://hibernate.org/orm/) for persistence.
* [Jersey](https://jersey.java.net/) for REST endpoints.
* [Swagger UI](https://github.com/swagger-api/swagger-ui) for manual API testing and API documentation.
* [JUnit](http://junit.org/junit4/) for unit testing.
* [Mockito](http://mockito.org/) for mocked testing.
* [Rest Assured](https://github.com/rest-assured/rest-assured) for integration testing.

## How to Build and Run the Project

This project uses the [Gradle](https://gradle.org) build system, you can build the project locally just by typing the
following in the console:

```
./gradlew build
```

On Windows use `gradlew.bat` instead of `./gradlew`.

The output of the Gradle build is located in the  `build/` directory.

To run the project deploy the war-File in the `build/libs` directory to the application server of your choice.
Alternatively, you can use the Gradle Jetty plugin:

```
./gradlew jettyRun
```

### Swagger

Browse to the application root for API documentation:

[http://localhost:8080/](http://localhost:8080)

All REST endpoints can be tested locally with the Swagger UI frontend.

## How to Extend the Project

If you want to import the project in an IDE such as Eclipse or IntelliJ IDEA then Gradle provides a way to generate all
the necessary project files.

Generate Eclipse project:
```
./gradlew cleanEclipse eclipse
```

Generate IntelliJ IDEA project:
```
./gradlew cleanIdea idea
```

Alternatively, with IntelliJ IDEA you can also import the project from the Gradle model,
just follow [this guide](https://www.jetbrains.com/help/idea/2016.1/importing-project-from-gradle-model.html).

## Tasks

You have to write a full working backend for managing exercises.
You will be provided with a working code skeleton and you will need to implement following functionalities:

1. Task:
    - Persist new exercises:
        - Insert new exercises. (+)
        - Update exercise. (+)
        - All exercise fields should be mandatory for inserting (except the id) and updating. (+)
        - Use an appropriate HTTP status code if the input is invalid. (+)
        - Check if the provided description has a valid syntax (only alphnum + spaces) (+)
          by using a simple regular expression. (+)
        - During exercise persisting it should be checked that there
          is no other exercise already present for the user id, in the
          period (start + duration) where the new exercise will take place.
          If this is the case return an HTTP status code `Conflict` with appropriate
          error message. (+)
        - While updating an exercise, the user id and type shouldn't change.(+)
    - Delete an existing exercise by a given exercise id. (+)
    - List all existing exercises for a given user id. (+)
        - Add the ability to filter them by type and/or by date. (+)
    - Adapt the methods of TestClientService in the test environment to run all tests of the ExerciseBasicTest class. (+)
        - Remove the @Ignore annotations. (+)
2. Task
    - Create a REST end point in the ExerciseService with the following logic: (+)
        - Define a way to rank a list of user ids by the users' points: (+)
        - the points are calculated as follows: (+)
            - A user gets points for each exercise he has completed in the past 4 weeks. (+)
            - A user gets one point per minute of the duration of the exercise plus the burnt kilo calories. (+)
            - Each time a user performs the same type of exercise again it is worth 10% less (Make sure to look at the newest exercises first). (+)
                Example: A user ran 4 times in the past 4 weeks (let's say once per week).
                This weeks run is worth 100%. The oldest run is worth only 70% of the calculated points for the exercise. (+)
            - An exercise can't count for less than 0 points. (+)
        - Each exercise type has a multiplication factor for the point calculation: (+)
            - RUNNING - 2
            - CYCLING - 2
            - SWIMMING - 3
            - ROWING - 2
            - WALKING - 1
            - CIRCUIT_TRAINING - 4
            - STRENGTH_TRAINING - 3
            - FITNESS_COURSE - 2
            - SPORTS - 3
            - OTHER - 1
        - return a list of user ids. Order them according to the users' points in descending order. (+)
    - Adapt the method of TestClientService in the test environment to run all tests of the RankingBasicTest class. (+)
        - Remove the @Ignore annotations. (+)
3. Task (Optional)
    - Create a feature of your own design and have fun!
        Not a feature. But in general, technically :
        - Added @HasId, @HasNoId custom validation possibilities
        - fixed warning with rest empty Path in the logs
        - fixed issue with the field distance in TestData, that should not be null/empty
        - tried to provide good javadoc documentation on interface level
        - tried to provide good REST API documentation
        - added separate ValidationExceptionMapper for Validation exceptions
        - using java 8 features
        - implemented conflict check for both create and update exercise methods
        - for Task 2 created version with Streaming API solution "getRankingWithStreamingApi"
        - tried to use java8 DateTime api, but the Date field uses old java Date api

## How to Submit Your Solution

Before submitting make sure your project builds successfully by executing the following command:
```
./gradlew clean build
```

Then zip the project, but before that make sure to remove all binary/compiled data by doing a clean:
```
./gradlew clean
```

Send your solution as zip file using the [this online form](https://script.google.com/a/macros/egym.de/s/AKfycbwVMmgwNOQ_iq1Vk3z-FyJU_eQdx8QvzVUx9wbaFEXFNxi0uYnc/exec).
In order to avoid any misunderstandings, please fill out the same email and name you used in your application.

## Evaluation Criteria

- All tasks have to be finished.
- Your solution should be free of bugs.
- Each test has to run without failure.
  - The TestClientService class must be implemented for that.
    - Please don't change the method definition of the TestClientService.
  - Your coding task will be tested by a large number of tests.
- You should implement proper error handling.
- Write properly styled code.
- Think of a good REST end point design. Make it smart and clear.
