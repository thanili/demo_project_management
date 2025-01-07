# Freelance Project Management API

## Description
Freelancers often manage multiple clients and projects simultaneously, making it challenging to keep track of deadlines, deliverables, communications, and payments. This project provides a combined API and UI solution for organizing freelance workflows, without the complexity and cost of enterprise-level software.

## Objective
The **Freelance Project Management API** is built using Spring Boot to allow freelancers to:

* Manage clients and their details.
* Create and track multiple projects associated with clients.
* Manage tasks and milestones for each project.
* Track invoices, payments, and outstanding balances for projects.
* Set project deadlines and receive alerts for overdue tasks.

The platform includes both API endpoints and a web-based UI for intuitive interaction.

## Technologies and Libraries Used

### Backend:
* **Spring Boot**: Core framework for building the RESTful API.
* **Spring Data JPA**: Object-relational mapping (ORM) for database interactions.
* **Spring Security**: For authentication and role-based access control (optional, if security features are included).
* **H2/PostgreSQL/MySQL**: Database options for storing client, project, task, and invoice data.
* **Jackson**: Handles JSON serialization and deserialization.
* **Swagger/OpenAPI**: Provides interactive API documentation.
* **JUnit**: For unit and integration tests to ensure API functionality.

### Frontend:

* **Thymeleaf**: For server-side rendering of HTML templates.
* **CSS**: For styling the UI.

## API Endpoints

### Client Management

Freelancers need to manage multiple clients and have access to basic client information.

* `GET /clients`: Retrieve all clients.
* `POST /clients`: Add a new client.
* `GET /clients/{id}`: Retrieve a specific client by ID.
* `PUT /clients/{id}`: Update a client's information.
* `DELETE /clients/{id}`: Remove a client.

### Project Management

Freelancers need to create and track various projects, each associated with a client.

* `GET /projects`: Retrieve all projects.
* `POST /projects`: Create a new project.
* `GET /projects/{id}`: Retrieve project details by ID.
* `PUT /projects/{id}`: Update project information.
* `DELETE /projects/{id}`: Remove a project.

### Task and Milestone Management

Projects consist of multiple tasks or milestones that need tracking.

* `GET /projects/{projectId}/tasks`: Retrieve all tasks for a project.
* `POST /projects/{projectId}/tasks`: Add a task to a project.
* `PUT /projects/{projectId}/tasks/{taskId}`: Update a task.
* `DELETE /projects/{projectId}/tasks/{taskId}`: Remove a task.

### Invoice and Payment Tracking

Freelancers can generate invoices for completed projects or tasks and track client payments.

* `POST /projects/{projectId}/invoices`: Generate an invoice for a project.
* `GET /invoices`: Retrieve all invoices.
* `GET /invoices/{id}`: Retrieve a specific invoice.
* `PUT /invoices/{id}`: Mark an invoice as paid or update invoice details.

### Authentication and Role Management

This feature provides authentication to secure data, especially when handling sensitive financial information.

* `POST /auth/signup`: Register a new user.
* `POST /auth/login`: Log in and obtain access and refresh tokens for authentication.
* `POST /auth/refresh-token`: Get a new access token using a refresh token.

## Pagination and Sorting

**TO DO**: Implement pagination and sorting for endpoints that return lists of data, such as clients, projects, and tasks.

## UI Layer

### UI Templates
* `login.html`: Template for user authentication.
* `clients.html`: Displays a list of clients.
* `edit-client.html`: Form to create or update a client.
* `client-projects.html`: Displays a list of projects for a specific client.
* `edit-project.html`: Form to create or update a project.
* `project-tasks.html`: Displays a list of tasks for a specific project.
* `edit-task.html`: Form to create or update a task.
* `project-invoices.html`: Displays a list of invoices for a specific project.
* `edit-invoice.html`: Form to create or update an invoice.

**Features:**
* Navigation menus with links to core functionalities.
* User-friendly forms for CRUD operations.
* Logout functionality integrated into the navigation bar.

### Web Controllers
* `LoginWebController`: Handles login-related pages.
* `ClientWebController`: Manages client-related pages.
* `ProjectWebController`: Manages project-related pages.
* `InvoiceWebController`: Manages invoice-related pages.
* `ProjectTaskWebController`: Manages task-related pages.

## Entities

### 1. Client Entity

Represents a freelancer’s client. Each client can have multiple associated projects.

**Relationships**:
* One client can have many projects (one-to-many relationship).

### 2. Project Entity

Represents a project associated with a client. Each project can have several tasks and invoices.

**Relationships:**
* One project belongs to one client (many-to-one).
* One project has multiple tasks (one-to-many).
* One project can have multiple invoices (one-to-many).

### 3. Task Entity

Represents a unit of work to be completed for a project.

**Relationships:**
* One task belongs to one project (many-to-one relationship).

### 4. Invoice Entity

Represents payment tracking for a freelancer's services in relation to a project.

**Relationships:**
* One invoice belongs to a project (many-to-one relationship).

## Security

### Authentication

The **API** is protected using **JWT (JSON Web Token) for token-based authentication**, involving both access tokens (short-lived) and refresh tokens (long-lived).

* **Access tokens**: Used for short-term access to protected resources (e.g., 15 minutes).
* **Refresh tokens**: Used to request new access tokens when the current one expires (e.g., 7 days).

The **UI** is protected using **form-based login with session management**.
* Custom login page (`/hub/login`).
* Logout functionality (`/hub/logout`).

### JWT Authentication Flow

#### 1. Login and Access Token Request

* User makes a `POST` request to `/auth/login` with a username and password.
* Upon successful authentication, the response includes two tokens:
  * Access token (short-lived).
  * Refresh token (long-lived).

Example:
`POST /auth/login`
```json
{
  "username": "user",
  "password": "password"
}
```
Response:
```json
{
"accessToken": "eyJhbGciOiJIUzI1NiIsInR...",
"refreshToken": "eyJhbGciOiJIUzI1NiIsInR..."
}
```

#### 2. Using the Access Token for API Requests

* Attach the access token in the Authorization header to authenticate API requests:
 
`GET /api/clients`

`Authorization: Bearer <accessToken>`

#### 3. Refreshing the Access Token

* When the access token expires, request a new one using the refresh token.

`POST /auth/refresh-token`

`Refresh-Token: <refreshToken>`

Response:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR..."
}
```
#### 4. Expired Refresh Token

* If the refresh token has expired, the user must log in again.

### Key Classes
* **SecurityConfiguration**: Configures security settings for Spring Security. To handle both API and UI authentication two different SecurityFilterChain beans have been implemented.
* **JwtAuthenticationFilter**: Intercepts and validates JWT tokens for authentication.
* **JwtUtils**: Provides utility methods for generating, parsing, and validating JWT tokens.
* **CustomUserDetails**: Custom implementation of UserDetails for Spring Security.
* **CustomUserDetailsService**: Loads user details from the database for authentication.
* **AuthController**: Provides APIs for login and token generation.

## Authorization
**TO DO**: Implement role based access control (RBAC) using Spring Security to restrict access to certain endpoints based on user roles.

Roles:
* **ROLE_USER**: Default role for authenticated users (read).
* **ROLE_ADMIN**: Admin role with additional privileges (create, update, delete, read).

## Exception Handling

**GlobalExceptionHandler**: 

A centralized exception handler using @ControllerAdvice. 

It captures exceptions and returns appropriate HTTP responses, such as 404 Not Found or 400 Bad Request.

**Issue**: 

The TokenExpiredException and potentially other JWT-related exceptions may not be caught by the GlobalExceptionHandler and instead return a 401 Unauthorized response without specific messages. 

This might be due to how Spring Security's OncePerRequestFilter (i.e., JwtAuthenticationFilter) handles exceptions.

**Solutions:**

* Handle exceptions within the JwtAuthenticationFilter (current approach).
* Propagate exceptions to the global exception handler (TO DO).

## API Documentation (Swagger/OpenAPI)

Interactive API documentation is generated using Swagger (SpringDoc OpenAPI). Swagger UI can be accessed at:
http://localhost:8080/swagger-ui.html