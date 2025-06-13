# Customer Registry Server
---
## 📦 Description
This is the main Ktor application for managing customers. It includes endpoints for:
* GET /customers
* GET /customers/{id}
* POST /customers
* PATCH /customers
* DELETE /customers/{id}
---
## 🛠️ Technologies Used
* Kotlin + Ktor: HTTP server
* Exposed: ORM for PostgreSQL
* PostgreSQL: Persistent store
---
## 📄 Environment Variables
| Variable      | Description            |
|---------------|------------------------|
| `DB_URL`      | JDBC connection string |
| `DB_DRIVER`   | Postgres driver string |
| `DB_USER`     | Database username      |
| `DB_PASSWORD` | Database password      |
