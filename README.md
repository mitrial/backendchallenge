# backendchallenge
##Backend challenge for shop apotheke
plan:
1. minimal implementation of the Service Specification (only make a forwarding request to the GitHub API)
2. integration test example including a mock for live requests
3. add a distributed cache to fulfill the performance and scalability requirement
4. persistence layer to save the results of 1.
5. create a docker container for the service
6. add input validator and error handling
7. extend tests

After 4 hours I finished the first 3 tasks and fulfill the Service Specification, scalability and performance
 requirements
 

##Thinks I would like to change 
* Manually written response classes. They should be auto-generated from an external API like OpenAPI (but the initial
 creation of these needs time I don't want to spend here)
* Testing. Currently, I only finished the simple example of an Integration test. Usually, every class with internal logic should have a unit test.

* The persistence layer.
I wanted to decouple the service, and the GitHub API to ensure a constant performance and resilience.
I need too much time for the first 2 steps.