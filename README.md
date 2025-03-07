# Hostfully Test APIs

![example](https://github.com/leonardocamargo/hostfully-test-apis/actions/workflows/automation-apis.yml/badge.svg)

This repository contains automated tests for Hostfully APIs using Java, Rest Assured, JUnit 5, and Allure for reporting. The project is structured in a modular way and leverages GitHub Actions for continuous integration (CI) and automatic publishing of the Allure report to GitHub Pages.

### Table of Contents

[Overview](#overview)

[Technologies](#technologies)

[Project Structure](#project-structure)

[Environment Configuration](#environment-configuration)

[Running Tests Locally](#running-tests-locally)

[GitHub Actions Workflow](#github-actions-workflow)

[Allure Report](#allure-report)



## Overview


The Hostfully Test APIs project is designed to validate Hostfully API endpoints using automated tests. The tests can run in different environments (qa, staging, dev, etc.) by loading specific properties files. Additionally, the test results are collected by Allure to generate an HTML report, which is then automatically deployed to GitHub Pages via a GitHub Actions workflow.


## Technologies

- Java 17 – Programming language
- Gradle – Build tool and dependency management
- JUnit 5 – Testing framework
- Rest Assured – Library for testing REST APIs
- Allure – Reporting tool for test results
- GitHub Actions – CI/CD automation
- Lombok – Reduces boilerplate code in Java
- SLF4J (NOP) – Logging (suppresses unnecessary log output)



## Project Structure 
```
hostfully-test-apis/
├── app/  

│   ├── src/

│   │   ├── main/

│   │   │   └── java/         # Application code (if any)

│   │   └── test/

│   │       └── java/         # Automated tests

│   │           ├── api/     # Apis file to reduce code duplication, where we call the apis

│   │           ├── base/     # BaseTest class with shared configurations

│   │           ├── pojos/    # POJOs to map JSON responses

│   │           └── tests/    # Test cases

│   │           └── utils/    # utilities classes


│   └── build/                # Gradle build outputs (including allure-results)

├── src/test/resources/       # Properties files (qa.properties, dev.properties, etc.)

│    ├── schemas/             # json schemas to match with responses


├── .github/

│   └── workflows/            # GitHub Actions workflows

├── build.gradle              # Gradle build configuration

└── README.md                 # This file
```

## Environment Configuration

Environment-specific settings (such as base URI, username, and password) are stored in properties files located in src/test/resources. For example, qa.properties might look like this:
```sh
baseUri=https://qa-assessment.svc.hostfully.com
username=xxxxx@hostfully.com
password=xxxxxxx
```

At runtime, the test code loads the appropriate properties file based on the value of the env property. You can pass the environment as a parameter (e.g., -Denv=qa or -Denv=staging). If no environment is specified, the project defaults to qa.

## Running Tests Locally

To run the tests locally, use the Gradle wrapper:

```sh
./gradlew clean test -Denv=qa
```


This command will load the settings from qa.properties and execute the tests accordingly.


> Note: ` -Denv` is necessary if you want to run in a different environment: dev, staging and production.


## GitHub Actions Workflow 


The project uses GitHub Actions to automatically run tests and deploy the Allure report. A typical workflow can be manually triggered (via workflow_dispatch) or triggered on push to the main branch.

For example, you can manually run the workflow from the Actions tab and provide the environment input (e.g., qa, staging, or dev). The workflow passes the environment variable to Gradle via -Denv.


## Allure Report Generation and Deployment 

After running tests, Allure collects test results (stored in app/build/allure-results) and generates an HTML report. A GitHub Actions workflow then publishes this report to GitHub Pages so that it can be viewed via a web URL.

#### Workflow Outline:

Pre-conditions Job: Sets up the environment and JDK.

Test Job: Runs tests, generates Allure results, and uploads them as artifacts.

Deploy Job: Downloads the artifacts, builds the Allure report (with history if configured), and deploys it to GitHub Pages.
Once deployed, the report is accessible at:

```sh
https://<your-username>.github.io/<repo-name>/
```
For instance, if your GitHub username is leonardocamargo and your repository is hostfully-test-apis, the report should be available at:
```sh
https://leonardocamargo.github.io/hostfully-test-apis/
```

#### Next steps

- Run with docker container
- send data to new relic
- jira integration (create ticket if test fails)
- add a logs library
  