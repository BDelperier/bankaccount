# bankaccount
Simple Bank Account Kata

## architecture
3 tiers application with a simplified Repository layer and without Spring Security.

The WebService layer is inside the package 'rest'.
The Service layer is inside the package 'service'.
The Repository layer is inside the package repository'.

Only 2 models used : the one for the WebServices, and the one for the Repository; the Service layer doing the mapping.


## Tests
There are Unit Tests around the Service layer.
There are some Light Integration Tests around the Rest layer (launching the SpringBoot and calling the WebService to test)


## Build and Launch

A simple 'mvn clean install' for a complete build.
To launch the application, proceed as usual for a SpringBoot.
