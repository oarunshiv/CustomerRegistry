## 🤝 Purpose
This module provides a type-safe client to interact with the Customer API and an integration test suite. 
Useful for internal services or third-party integrations.

---

## 📦 Features
- Type-safe calls to all endpoints
- Configurable base URL
- Retry + error handling
- `CustomerApiIntegrationTest` can be used to run integration test against any endpoint running the Customer Registry Server.
---

## 🛠️ Usage
```kotlin
val client = DefaultCustomerClient(baseUrl = "http://localhost:8080")
val customer = client.createCustomer("Alice", "alice@example.com")
```
---
## ▶️ Running Tests
From the root project:
```bash
./gradlew :customer-api-client:integrationTest -PbaseUrl=http://localhost:8080
```
If baseUrl is omitted, default value (`http://localhost:8080`) will be used.

---
## ♻️ Data Cleanup
Each test ensures cleanup (delete created records) or runs against isolated IDs. 
You can extend the @BeforeEach and @AfterEach hooks for test-level isolation.
---
## ✅ Tests Included

* Create customer
* Handle duplicate email
* Get all customers
* Get by ID (valid + invalid)
* Update by ID (valid + failure cases)
* Delete by ID (valid + failure cases)