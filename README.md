# Learning Management System (LMS)

![LMS Banner](Docs/image/lms.webp)

A web-based platform designed to facilitate online education and training. The LMS allows students to enroll in courses, access materials, submit assignments, and track progress, while instructors can create and manage courses, and administrators oversee the system.

Developed by **Khairul Basar** |

---

---

## ✨ Features

### 🎓 Student Features

* Register, login, and manage profile
* Browse and enroll in courses
* Access learning materials (videos, PDFs, documents)
* Submit assignments and take quizzes
* View progress, grades, and certificates
* Participate in discussion forums
* Receive system notifications

---

### 👨‍🏫 Instructor Features

* Create, update, and delete courses & modules
* Upload rich content (videos, files)
* Create assignments, quizzes, and grading rubrics
* Grade student submissions
* Monitor enrollments and student progress
* Engage with students via discussions

---

### 🛠️ Admin Features

* Manage users (students, instructors)
* Approve / reject instructors and courses
* Manage categories, tags, and content
* View system analytics and reports
* Generate certificates
* Manage global system settings

---

### ⚙️ Core System Features

* Secure JWT‑based authentication & role authorization
* Course enrollment and progress tracking
* Real‑time notifications and discussion forums
* File uploads (local or AWS S3)
* Automatic certificate generation on course completion
* Responsive and user‑friendly UI

---

## 🧱 Technology Stack

| Layer      | Technologies                                               |
| ---------- | ---------------------------------------------------------- |
| Backend    | Java 17/21, Spring Boot 3.x                                |
| Security   | Spring Security, JWT                                       |
| Frontend   | Thymeleaf, HTML5, CSS3, Bootstrap (or React if integrated) |
| Database   | MySQL / PostgreSQL                                         |
| Build Tool | Gradle                                                     |
| ORM        | Spring Data JPA, Hibernate                                 |
| Other      | Lombok, WebSocket (optional), AWS S3 (file storage)        |
| Deployment | Docker, AWS / Render / Kubernetes                          |

---

## 🗂 Project Structure

```
learning-management-system/
├── Docs/                    # Additional documentation, diagrams, ERD
├── gradle/                  # Gradle wrapper
├── src/
│   ├── main/
│   │   ├── java/            # Controllers, Services, Entities, Repositories, Config
│   │   └── resources/       # application.yml, static/, templates/ (Thymeleaf)
│   └── test/                # Unit & integration tests
├── build.gradle             # Dependencies & build configuration
├── settings.gradle
├── gradlew / gradlew.bat
├── .gitignore
├── LICENSE
└── README.md
```


## 🛠️ Prerequisites

* Java 17 or higher
* Gradle 8+
* MySQL 8+ or PostgreSQL (or Docker)
* IDE: IntelliJ IDEA / Eclipse
* (Optional) Node.js if frontend is React‑based

---

## 🚀 Installation & Setup

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/KhairulBasharbd/learning-management-system.git
cd learning-management-system
```

### 2️⃣ Configure Database

Create a database (example: `lms_db`) and update:

`src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/lms_db
    username: root
    password: yourpassword
```

### 3️⃣ Build the Project

```bash
./gradlew build
```

### 4️⃣ Run the Application

```bash
./gradlew bootRun
```

➡ Access the application at: **[http://localhost:8080](http://localhost:8080)**

---

### 🔑 Default Admin Credentials

> ⚠️ Change immediately after first login

* **Email:** [admin@lms.com](mailto:admin@lms.com)
* **Password:** admin123

---

## 🌐 Usage & API Endpoints (Examples)

### 🔐 Authentication

* `POST /api/auth/register` → Student / Instructor signup
* `POST /api/auth/login` → Get JWT token
* `GET /api/auth/me` → Current user info

---

### 📚 Courses

* `GET /api/courses` → List all courses
* `GET /api/courses/{id}` → Course details
* `POST /api/courses` → Create course (Instructor)
* `POST /api/enrollments` → Enroll in course (Student)

---

### 📝 Assignments & Quizzes

* `POST /api/assignments/submit` → Submit assignment
* `POST /api/quizzes/{id}/attempt` → Attempt quiz
* `GET /api/grades/my` → View grades

---

### 🛡️ Admin

* `GET /admin/dashboard` → Admin dashboard
* `POST /admin/users` → Manage users

> Use **Postman** or **Swagger/OpenAPI** (if enabled) for full API documentation.

---

## 🧪 Testing

Run unit and integration tests:

```bash
./gradlew test
```

Includes:

* Service‑level unit tests
* Controller integration tests

---

## 📦 Deployment

### Docker (Recommended)

* Add `Dockerfile` and `docker-compose.yml`
* Container‑ready for production

### Cloud Deployment

* Render / Railway
* AWS Elastic Beanstalk / EC2
* Kubernetes

### Manual Deployment

```bash
./gradlew bootJar
java -jar build/libs/lms-*.jar
```

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch

   ```bash
   git checkout -b feature/add-video-lecture
   ```
3. Commit your changes
4. Push and open a Pull Request

Follow clean code practices, add tests, and update documentation.

---

## 📄 License

MIT License – see the `LICENSE` file for details.

---

## 👨‍💻 Author

**Khairul Bashar**
Backend Engineer | Java & Spring Boot Specialist

📍 Dhaka, Bangladesh
📅 February 2026

🔗 GitHub: [https://github.com/KhairulBasharbd](https://github.com/KhairulBasharbd)
🔗 Repository: [https://github.com/KhairulBasharbd/learning-management-system](https://github.com/KhairulBasharbd/learning-management-system)

---

🎉 *Happy learning & teaching! Build knowledge, scale impact.* 🚀
