# Contact, Task, and Appointment Services

Original project from CS 320: Software Testing, Automation, and Quality Assurance at Southern New Hampshire University (Project One). This version is the starting point for my CS 499 Capstone software design and engineering enhancement.

## Overview

Three in-memory Java services meant to serve as the back end of a mobile application. Each service has a model class that validates its own fields and a service class that stores objects in a `HashMap`, with JUnit 5 tests covering every requirement.

| Service | Model rules |
|---|---|
| **Contact** | ID up to 10 characters (required, not updatable), first and last name up to 10 characters, phone exactly 10 digits, address up to 30 characters |
| **Task** | ID up to 10 characters (not updatable), name up to 20 characters, description up to 50 characters |
| **Appointment** | ID up to 10 characters (not updatable), date cannot be in the past, description up to 50 characters |

Each service supports adding with a unique ID, deleting by ID, and updating the allowed fields by ID.

## Project structure

```
ContactService/       Contact.java, ContactService.java, ContactTest.java, ContactServiceTest.java
TaskService/          Task.java, TaskService.java, TaskTest.java, TaskServiceTest.java
AppointmentService/   Appointment.java, AppointmentService.java, AppointmentTest.java, AppointmentServiceTest.java
```

All classes use the package `projectone`.

## Requirements

- Java 17 or later (JDK)
- VS Code (or any terminal with PowerShell)

## Running the tests in VS Code

Open the repository folder in VS Code, open the terminal (**Ctrl + `**), and follow the steps below.

### First time only

Download the JUnit runner. It is saved to your Temp folder, so this only needs to be repeated if Temp is cleared or you are on a different computer.

```
$url = "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar"
curl.exe -L -o "$env:TEMP\junit.jar" $url
```

### Every time

Compile the classes and tests. Run this again whenever a `.java` file changes, or the tests will run against the old version.

```
javac -d "$env:TEMP\cs320out" -cp "$env:TEMP\junit.jar" ContactService\*.java TaskService\*.java AppointmentService\*.java
```

Run all tests:

```
java -jar "$env:TEMP\junit.jar" execute -cp "$env:TEMP\cs320out" --scan-classpath
```

All 36 tests should pass.

## Known limitations

These are planned for the CS 499 enhancement:

- The services only exist as classes called by unit tests, so no application can use them and data is lost when the program ends.
- All three service classes repeat the same add, delete, update, and lookup logic.
- Validation errors are thrown as `IllegalArgumentException` with no conversion to a response a client could use.
- `HashMap` is not thread-safe for concurrent requests.
- `Appointment` uses the outdated `java.util.Date`.
- `Task` and `Appointment` accept an empty string as an ID, while `Contact` rejects it.
- There is no authentication, so anyone with access to the services can change any record.
- The folder structure does not match the `projectone` package, and there is no build file such as Maven.