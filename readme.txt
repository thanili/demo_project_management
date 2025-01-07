Description

Freelancers often juggle multiple clients and projects, making it difficult to keep track of deadlines, deliverables, client communications, and payments. Create a simple, personalized solution to organize their workflow without the complexity and cost of enterprise-level software.

Objective:
Create a Freelance Project Management API using Spring Boot that allows freelancers to:
- Manage clients and their details.
- Create and track multiple projects associated with clients.
- Manage tasks and milestones for each project.
- Track invoices, payments, and outstanding balances for projects.
- Set project deadlines and receive alerts for overdue tasks.

This API follow RESTful principles, utilizing JSON for data exchange, and can be extended for future features like reporting or client notifications.

Technologies and Libraries used:
- Spring Boot: To build the core RESTful API.
- Spring Data JPA: For ORM and database interactions.
- Spring Security: For authentication and role-based access control (if you want to include security features).
- H2/ PostgreSQL/ MySQL: As the database to store client, project, task, and invoice data.
- Jackson: To handle JSON serialization and deserialization.
- Swagger/OpenAPI: For API documentation, so users can see and interact with the API via a user-friendly interface.
- JUnit: For unit and integration tests to ensure the API works correctly.

API endpoints
- Client Management: Freelancers need to manage multiple clients and have access to basic client information.
	- GET /clients: Retrieve all clients.
	- POST /clients: Add a new client.
	- GET /clients/{id}: Retrieve a specific client by ID.
	- PUT /clients/{id}: Update a client's information.
	- DELETE /clients/{id}: Remove a client.

- Project Management: reelancers need to create and track various projects, each associated with a client.
	- GET /projects: Retrieve all projects.
	- POST /projects: Create a new project.
	- GET /projects/{id}: Retrieve project details by ID.
	- PUT /projects/{id}: Update project information.
	- DELETE /projects/{id}: Remove a project.
	
- Task and Milestone Management: Projects involve multiple tasks or milestones that need tracking.
	- GET /projects/{projectId}/tasks: Retrieve all tasks for a project.
	- POST /projects/{projectId}/tasks: Add a task to a project.
	- PUT /projects/{projectId}/tasks/{taskId}: Update a task.
	- DELETE /projects/{projectId}/tasks/{taskId}: Remove a task.
	
- Invoice and Payment Tracking: Freelancers need a way to create invoices for completed projects or tasks and track payments from clients.
	- POST /projects/{projectId}/invoices: Generate an invoice for a project.
	- GET /invoices: Retrieve all invoices.
	- GET /invoices/{id}: Retrieve a specific invoice.
	- PUT /invoices/{id}: Mark an invoice as paid or update details.

- Authentication and Role Management (Optional Advanced Feature): The API may require authentication to ensure data is secure, especially when handling sensitive financial information.
	- POST /auth/signup: Register a new user.
	- POST /auth/login: Log in to get access and refresh tokens for authentication.
	- POST /auth/refresh-token: Get a new access token using the refresh token.

Pagination and Sorting
TO DO.

Entities
1. Client Entity:
- A Client represents a freelancer’s client. A client can have multiple projects.
- Relationships: 
	- One Client can have many Projects. It’s a one-to-many relationship.

2. Project Entity:
- A Project is associated with a client and has several tasks. Each project can also have invoices.
- Relationships:
	- One Project belongs to one Client (one-to-one).
	- One Project has multiple Tasks (one-to-many).
	- One Project can have multiple Invoices (one-to-many).
	
3. Task Entity:
- A Task is associated with a project. It represents a unit of work that needs to be completed for the project.
- Relationships: 
	- One Task belongs to one Project (many-to-one relationship).
	
4. Invoice Entity:
- An Invoice is associated with a project and tracks payments for the freelancer’s services.
- Relationships: 
	- One Invoice belongs to a Project (many-to-one relationship).

Security
Authentication
API is protected using JWT (JSON Web Token) token-based authentication mechanism with access (short living) and refresh (long living) tokens.
- Short-lived access tokens (e.g., 15 minutes) are used to access protected resources.
- Long-lived refresh tokens (e.g., 7 days) are used to request new access tokens when the current access token expires.

When a client's access token expires, they send the refresh token to an endpoint (/api/auth/refresh-token) to get a new access token and use it to access protected resources.

JWT Authentication Flow:
1. Login and Access Token Request
- User makes a POST request to /api/auth/login with a username and password.
- If authentication succeeds, two JWT tokens are generated and returned in the response: access token (short living) and refresh token (long living).

Request:
POST /api/auth/login
{
  "username": "user",
  "password": "password"
}
Response:
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR..."
}

2. Using the Access Token for API Requests
- After logging in, the client uses the access token to authenticate requests to protected resources. For example, to fetch all the clients from the database, the client sends a request with the access token in the Authorization header:

Request:
GET /api/clients
Authorization: Bearer <accessToken>

Response:
[
    {
        "id": 1134,
        "name": "Client 1",
        "email": "client1@example.com",
        "phone": "1234567891",
        "projectIds": [
            11201,
            11202,
            11203,
            11204,
            11205,
            11206,
            11207,
            11208,
            11209,
            11210
        ]
    },
	{...}
]

If the access token is still valid, this request works. However, when the access token expires, the client will get a 401 Unauthorized response.

3. When the Access Token Expires
Once the access token expires, the client should use the refresh token to obtain a new access token by making a request to the refresh token endpoint.
The client makes a request to the /api/auth/refresh-token endpoint, sending the refresh token (in the header) to get a new access token.

Request:
POST /api/auth/refresh-token
(HeadeR) Refresh-Token: <refreshToken>

Response:
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR..."
}

4. Replace the Expired Access Token
Once the client receives the new access token in response to the refresh token request, it replaces the old, expired access token. The client continues to use this new access token in future API requests.

5. Refresh Token Expiration
Refresh tokens are long-lived but can still expire. If the refresh token itself has expired, the user will be forced to log in again. In this case, a 401 Unauthorized will be returned from the refresh token endpoint as well.

Expired Refresh Token Response:
401 Unauthorized

When the refresh token is expired or invalid, the client has to log in again by making a fresh request to the /api/auth/login endpoint.

Key classes
- SecurityConfiguration:  Security configuration class for setting up Spring Security.
- JwtAuthenticationFilter: Filter for JWT authentication. This filter intercepts requests to validate the JWT token and authenticate the user.
- JwtUtils: Utility class for JWT operations: generating, parsing, and validating JWT tokens.
- CustomUserDetails: Custom implementation of UserDetails for Spring Security.
- CustomUserDetailsService: Service to load user details from the database and use it for authentication.
- AuthController: API for login and generate JWT tokens.

Authorization
Role based authorization: TO DO.

Exception Handling
GlobalExceptionHandler: Used @ControllerAdvice to catch exceptions and return meaningful HTTP responses with appropriate status codes (e.g., 404 for NotFound, 400 for BadRequest) and useful messages to the client.

(*) There is an issue with the TokenExpiredException and potentially other JWT-related exceptions which are being thrown but not caught by the GlobalExceptionHandler. Instead, the client gets a 401 error without any specific messages. This could be due to how Spring Security and the OncePerRequestFilter (i.e., JwtAuthenticationFilter) handle exceptions. The key issue lies in how exceptions are propagated from the JwtAuthenticationFilter. In Spring Security, exceptions thrown during the filter chain (like those in your JwtAuthenticationFilter) might not reach the @RestControllerAdvice handlers (GlobalExceptionHandler) because Spring Security manages them separately.

Solutions:
1. Handle Exceptions in JwtAuthenticationFilter Manually (as we do now).
2. Propagate Exceptions to the Global Exception Handler (TO DO).

API Documentation (Swagger/OpenAPI)
Use Swagger (SpringDoc OpenAPI) to generate interactive API documentation. Swagger UI is accessible at http://localhost:8080/swagger-ui.html