# sc-common-sc-temporal-test

We regularly use the Saga pattern for designing microservice architectures. How do we implement this pattern? 
We tend to use Pub-Sub messaging models for Event-based choreography. Also, we use systems like Netflix Conductor for Orchestration.

Over the past few months, I have developed a new perspective on designing systems with Temporal workflows. Temporal is a workflow orchestration engine that can support Event-based processing and also help us create Human dependent long-running workflows. To illustrate this, I have picked up the topic of designing a data ingestion pipeline and tried to develop this system with Temporal.

for detailed discreption please checkout my article

https://www.linkedin.com/pulse/event-driven-reactive-data-ingestion-pipelines-tarun-annapareddy/
