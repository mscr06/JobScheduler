# JobScheduler
Java API to schedule and cancel jobs

# Fixed few bugs

Spring Boot Application when started will run two scheduled jobs
task1
task2

Use Swagger UI to start/stop Jobs
Location: http://localhost:8080/swagger-ui.html

1. /demo/scheduler/start will be starting a job, give input as task1 or task2
2. /demo/scheduler/stop will stop a job, give input as task1 or task2
3. /demo/scheduler/startTimer will start job using TimerTask
