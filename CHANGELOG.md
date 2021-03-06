# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2021-06-21
### Added
 - Column `available_credit_limit` added to the ACCOUNT table;
 - Functionality Available Credit Limit added;
 - Creation integration test and refactoring of unity test for the new functionalities; 
 - Changing (Long -> Enum) of the Transaction API contract;

## [0.3.0] - 2021-06-18
### Added
 - Project adaptation for Docker container

## [0.2.0] - 2021-06-16
### Added
 - Transaction API: transaction creation funcionality. Creation of unit and integration test;

## [0.1.0] - 2021-06-15
### Added
 - Mapping account and transaction entity;
 - Integration test added with H2;
 - SQL script creation with Flyway
 - PMD added in the project;
 - GithubActions workflow added in the project;
 - Account API: creation and search feature. Creation of unit and integration test;
