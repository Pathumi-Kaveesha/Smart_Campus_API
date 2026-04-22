# 📘 Smart Campus API
## 🧠 Overview

This is a RESTful Smart Campus API built using Java EE (JAX-RS / Jersey) and deployed on Apache Tomcat.

The system manages:

🏢 Rooms

📡 Sensors

📊 Sensor Readings

#### Relationships:

One Room -> many Sensors

One Sensor -> many Readings

All data is stored in-memory (no database required).

### 📥 How to Run 
1. Clone the Repository: git clone https://github.com/Pathumi-Kaveesha/Smart_Campus_API.git
   
Then open NetBeans: File -> Open Project

Select the cloned folder

2. Build Project
   
In NetBeans: Right click project

Click Clean and Build

Wait until Maven downloads dependencies automatically.

3. Configure Tomcat Server
   
If not already configured:

Go to Services tab

Right click Servers -> Add Server

Choose Apache Tomcat 9

Finish setup

4. Run the Project

Right click project

Click Run

NetBeans will deploy automatically to Tomcat.

5. Access API

Open browser: http://localhost:8080/SmartCampusAPI/api/v1/

## 📌 API Endpoints
### 🏢 Rooms
GET /api/v1/rooms
POST /api/v1/rooms
GET /api/v1/rooms/{id}
DELETE /api/v1/rooms/{id}
GET /api/v1/rooms/{id}/sensors

Sample body for room creation
{
  "id": "R1",
  "name": "Main Hall",
  "capacity": 100
}

### 📡 Sensors
GET /api/v1/sensors

GET /api/v1/sensors?type={type}

POST /api/v1/sensors

DELETE /api/v1/sensors/{id}

PATCH /api/v1/sensors/{id}/status


Sample body for sensor creation

{
  "id": "S1",
  "type": "Temperature",
  "status": "ACTIVE",
  "currentValue": 25.0,
  "roomId": "R1"
}

### 📊 Sensor Readings
GET /api/v1/sensors/{id}/readings

POST /api/v1/sensors/{id}/readings

Sample body for adding sensor reading

{
  "id": "READ1",
  "value": 25.5
}

### 🔍 API Discovery
GET /api/v1/


## 🧪 Sample CURL Commands
### Create Room

curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id":"R1","name":"Lecture Hall", "capacity": 100}'

### Create Sensor

curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id":"S1","type":"temperature","roomId":"R1","status":"ACTIVE", "currentValue": 25.0,}'

### Add Reading

curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/S1/readings \
-H "Content-Type: application/json" \
-d '{"id":"READ1","value":25.5}'

### Get Readings

curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/sensors/S1/readings

### Update Sensor Status
curl -X PATCH http://localhost:8080/SmartCampusAPI/api/v1/sensors/S1/status \
-H "Content-Type: application/json" \
-d '{"status":"MAINTENANCE"}'

## ⚠️ Important Notes

No database required (in-memory storage)

Data resets when server restarts

Must use NetBeans + Tomcat for evaluation

Ensure Maven dependencies load before running



# Questions and Answers
## PART 1
### Question 1
#### In your report, explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.

In JAX-RS (e.h., Jersey), the default lifecycle of a resource class is request-based, meaning a new instance is created for each incoming HTTP request. The runtime does not treat resource classes as singletons by default. However, depending on configuration (such as @Singleton or application scope providers), a resource can be made singleton, but this is not the default behavior.
Since each request creates a new resource instance, in-memory data structures must be declared as static (e.g., static Map<String, Sensor> sensors) to ensure data persistence across requests and shared access between different resource instances.
Because static collections are shared across threads:
•	Multiple requests may access/modify data simultaneously
•	This can lead to race conditions
To prevent issues synchronized blocks or thread safe structures like ConcurrentHashMap can be used.

### Question 2
#### Why is the provision of “Hypermedia” (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?

HATEOAS (Hypermedia As The Engine of Application State) is a key principle of REST where responses include links to related resources, allowing clients to navigate the API dynamically.
Benefits of HATEOAS:
•	Clients do not need hardcoded endpoint knowledge
•	API becomes self-descriptive and discoverable
•	Reduces dependency on external documentation
•	Allows backend evolution without breaking clients

In this API, the discovery endpoint returns:
•	API version
•	Contact information
•	Links to main resources (rooms, sensors, readings)
Instead of reading external docs, clients can discover how to use the API directly from responses, improving usability and flexibility.

## PART 2
###	Question 1
#### When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client side processing.

Returning full room objects provides richer contextual information in a single response, which improves usability for client applications. However, this approach increases network bandwidth usage and results in larger payload sizes, which may impact performance in large-scale systems.
In contrast, returning only room IDs reduces response size and improves overall scalability and efficiency. The downside is that clients must make additional API calls to retrieve full room details, increasing request overhead and complexity on the client side.
Overall, there is a trade-off between usability and performance. Full objects favor simplicity for the client, while IDs favor system efficiency and scalability.

### Question 2
#### Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.

The DELETE operation is considered idempotent in this implementation.
Once a room is deleted, it is permanently removed from the system state. If the same DELETE request is sent multiple times, the outcome remains consistent. The first request successfully deletes the room, while any subsequent requests will simply return a “room not found” response since the resource no longer exists.
Therefore, repeated execution of the same DELETE request does not produce any additional changes to the system state. This satisfies the definition of idempotency, where multiple identical requests result in the same final outcome.

## Part 3
### Question 1
#### We explicitly use the @Consumes (MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?
When a client sends data in a format other than application/json, such as text/plain or application/xml, the JAX-RS runtime will not process the request normally.
•	The request fails at the framework level and typically returns 415 Unsupported Media Type, since the endpoint explicitly expects JSON input defined by @Consumes(MediaType.APPLICATION_JSON). 
•	Jersey performs media type validation before invoking the resource method, meaning the controller logic is never executed if the format does not match. 
This mechanism ensures that only properly structured JSON payloads are accepted, preventing invalid data from entering the application and maintaining consistency across API interactions.

### Question 2
#### You implemented this filtering using @QueryParam. Contrast  this with an alternative design where the type is part of the URL path (e.g., /api/vl/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?
Using query parameters: GET /sensors?type=CO2
•	Designed specifically for filtering and searching within collections
•	More flexible, as multiple filters can be combined easily (e.g., type, status, roomId)
•	Keeps the base resource path clean and consistent
•	Supports optional parameters without changing the endpoint structure

Path-based alternative: /sensors/type/CO2
•	Less flexible when combining multiple filters
•	Leads to more rigid and deeper URL structures
•	Not suitable for optional or dynamic filtering conditions
Query parameters are generally more RESTful and practical for filtering collections because they provide flexibility without altering the core resource structure.

## Part 4
### Question 1
#### Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive con troller class?

The Sub-Resource Locator pattern is used to delegate nested resource handling to separate classes instead of keeping all logic inside one controller. This helps in organizing the API in a cleaner and more structured way, especially when dealing with hierarchical data like sensors and their readings.
It improves modularity by separating responsibilities, such as keeping sensor logic in SensorResource and reading logic in SensorReadingResource. This separation makes the code easier to maintain, extend, and debug. It also supports scalability, since additional nested features can be added without modifying a large central controller.
Without this pattern, a single controller would become very large and complex, especially with deeply nested endpoints like /sensors/{id}/readings, making the code harder to manage over time.

## Part 5
### Question 1

#### Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?
HTTP 422 Unprocessable Entity is often considered more semantically accurate than HTTP 404 in this case because the request itself is syntactically valid, but the data inside it is logically incorrect. A 404 status indicates that the requested resource cannot be found at the given endpoint, which applies to URL-level issues. However, when a client sends a valid JSON payload that contains a reference to a non-existent resource (for example, a roomId that does not exist), the problem is not the endpoint but the business logic validation of the request body.
Therefore, 422 is more appropriate because it clearly communicates that the server understands the request format, but cannot process it due to semantic errors in the provided data.

###	Question 2

#### From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?
From a cybersecurity standpoint, exposing internal Java stack traces to external API consumers is a serious risk because it reveals implementation details that should remain hidden. Stack traces are meant for debugging, not for production responses where security is important.
An attacker can use this information to understand the system structure, including class names, package structure, frameworks used (like Jersey or Spring), and internal components such as database or service layers. This helps them map the application and find possible weak points.
Stack traces may also expose sensitive details like file paths, configuration data, and method execution flow, which can help attackers craft targeted exploits. Therefore, production systems should return generic error messages while keeping full stack traces only in server-side logs.

### Question 3

#### Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?
Using JAX-RS filters for logging is more efficient because they centralize cross-cutting concerns like request and response tracking in one place instead of repeating code in every resource method. This keeps the API design cleaner and separates logging from business logic, making the code easier to read and maintain.
They also ensure consistency, since every request automatically passes through the filter, so no endpoint is accidentally left unlogged. This is something that can easily happen when using manual Logger.info() statements inside each method.
Overall, filters reduce duplication and make the system more scalable, because logging behavior can be updated in one place without modifying all resource classes.


