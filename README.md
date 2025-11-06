# OnlineCourseApp - REST API Documentation

A Spring Boot REST API for an Online Course Management System with JWT authentication, role-based access control, and AI-powered course summaries.

## 🚀 Features

- **User Authentication**: JWT-based authentication with role management (STUDENT, INSTRUCTOR, ADMIN)
- **Course Management**: Full CRUD operations for courses
- **Enrollment System**: Students can enroll in courses
- **AI Integration**: OpenAI-powered course summary generation
- **PostgreSQL Database**: Persistent storage with JPA/Hibernate
- **Security**: Spring Security with role-based access control

## 🛠️ Technology Stack

- Java 17+
- Spring Boot 3.2.0
- Spring Security (JWT)
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven
- Lombok
- OpenAI API

## 📁 Project Structure

```
src/main/java/com/example/onlinecourseapp/
├── config/              # Security, JWT, CORS configuration
├── controller/          # REST API endpoints
├── dto/                # Data Transfer Objects
├── model/              # JPA entities (User, Course, Enrollment)
├── repository/         # JPA repositories
├── service/            # Business logic
└── util/               # Helper classes (JwtUtil)
```

## 🔧 Setup & Configuration

### Prerequisites
- Java 17 or higher
- Maven
- PostgreSQL database

### Environment Variables
The application uses the following environment variables:
- `PGHOST`: PostgreSQL host
- `PGPORT`: PostgreSQL port  
- `PGDATABASE`: Database name
- `PGUSER`: Database username
- `PGPASSWORD`: Database password
- `OPENAI_API_KEY`: OpenAI API key (for AI summary feature)

### Running the Application
```bash
mvn spring-boot:run
```

The API will be available at: `http://localhost:8080`

## 📚 API Endpoints

### Authentication Endpoints (Public)

#### Register User
```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "STUDENT"
}
```

**Roles**: `STUDENT`, `INSTRUCTOR`, `ADMIN`

#### Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "john@example.com",
  "name": "John Doe",
  "role": "STUDENT",
  "message": "Login successful"
}
```

### Course Endpoints (Protected)

**Note**: All course endpoints require JWT token in Authorization header:
```
Authorization: Bearer YOUR_JWT_TOKEN
```

#### Get All Courses
```http
GET /api/v1/courses
```

#### Get Course by ID
```http
GET /api/v1/courses/{id}
```

#### Create Course (INSTRUCTOR/ADMIN only)
```http
POST /api/v1/courses
Content-Type: application/json

{
  "title": "Introduction to Java",
  "description": "Learn Java programming from scratch",
  "category": "Programming"
}
```

#### Update Course (Owner/ADMIN only)
```http
PUT /api/v1/courses/{id}
Content-Type: application/json

{
  "title": "Advanced Java Programming",
  "description": "Deep dive into Java advanced topics",
  "category": "Programming"
}
```

#### Delete Course (Owner/ADMIN only)
```http
DELETE /api/v1/courses/{id}
```

#### Get Courses by Instructor
```http
GET /api/v1/courses/instructor/{instructorId}
```

### Enrollment Endpoints (Protected)

#### Enroll in Course
```http
POST /api/v1/enroll
Content-Type: application/json

{
  "courseId": 1
}
```

#### Get My Enrollments
```http
GET /api/v1/enrollments
```

#### Get Enrollments by User ID
```http
GET /api/v1/enrollments/{userId}
```

### AI Endpoints (Protected)

#### Generate Course Summary
```http
POST /api/ai/summary
Content-Type: application/json

{
  "text": "This course covers advanced topics in Spring Boot including security, JPA, REST APIs, and microservices architecture."
}
```

**Response**:
```json
{
  "summary": "A comprehensive course on Spring Boot covering security implementation, database integration with JPA, RESTful API development, and microservices patterns."
}
```

## 🔐 Role-Based Access Control

| Endpoint | STUDENT | INSTRUCTOR | ADMIN |
|----------|---------|------------|-------|
| View Courses | ✅ | ✅ | ✅ |
| Create Course | ❌ | ✅ | ✅ |
| Update Own Course | ❌ | ✅ | ✅ |
| Update Any Course | ❌ | ❌ | ✅ |
| Delete Own Course | ❌ | ✅ | ✅ |
| Delete Any Course | ❌ | ❌ | ✅ |
| Enroll in Course | ✅ | ✅ | ✅ |
| AI Summary | ✅ | ✅ | ✅ |

## 🧪 Testing with Postman

1. **Register a user** using `/api/v1/auth/register`
2. **Login** using `/api/v1/auth/login` and copy the JWT token
3. **Set Authorization** header: `Bearer YOUR_JWT_TOKEN`
4. **Test endpoints** based on your user role

## 🗄️ Database Schema

### Users Table
- `id` (Primary Key)
- `name`
- `email` (Unique)
- `password` (Encrypted)
- `role` (STUDENT/INSTRUCTOR/ADMIN)

### Courses Table
- `id` (Primary Key)
- `title`
- `description`
- `category`
- `instructor_id` (Foreign Key → Users)
- `created_at`

### Enrollments Table
- `id` (Primary Key)
- `student_id` (Foreign Key → Users)
- `course_id` (Foreign Key → Courses)
- `enrolled_at`

## 🎯 Example Workflow

1. **Register as Instructor**:
   ```json
   POST /api/v1/auth/register
   {
     "name": "Professor Smith",
     "email": "smith@university.edu",
     "password": "secure123",
     "role": "INSTRUCTOR"
   }
   ```

2. **Login and get JWT token**:
   ```json
   POST /api/v1/auth/login
   {
     "email": "smith@university.edu",
     "password": "secure123"
   }
   ```

3. **Create a Course** (using JWT token):
   ```json
   POST /api/v1/courses
   Authorization: Bearer YOUR_TOKEN
   {
     "title": "Data Structures",
     "description": "Learn fundamental data structures",
     "category": "Computer Science"
   }
   ```

4. **Register as Student**:
   ```json
   POST /api/v1/auth/register
   {
     "name": "Jane Student",
     "email": "jane@student.edu",
     "password": "pass123",
     "role": "STUDENT"
   }
   ```

5. **Enroll in Course** (using student JWT):
   ```json
   POST /api/v1/enroll
   Authorization: Bearer STUDENT_TOKEN
   {
     "courseId": 1
   }
   ```

6. **Generate AI Summary**:
   ```json
   POST /api/ai/summary
   Authorization: Bearer YOUR_TOKEN
   {
     "text": "This course covers arrays, linked lists, stacks, queues, trees, and graphs."
   }
   ```

## ⚠️ Important Notes

- Passwords are encrypted using BCrypt
- JWT tokens expire after 24 hours (86400000 ms)
- The AI summary endpoint requires `OPENAI_API_KEY` environment variable
- Database tables are created automatically on first run
- SSL is required for PostgreSQL connection

## 🔒 Security Configuration

- JWT secret is configurable via `jwt.secret` property
- All endpoints except `/api/v1/auth/**` require authentication
- Passwords are never returned in API responses
- CORS is configured to allow all origins (configure for production)

## 📝 License

This project is created for educational purposes.
