# WarrantyWallet

A household warranty register built with **Java 21** and **Spring Boot**. Keep product details, purchase receipts, warranty dates, and repair history together in one place.

## Dashboard

![WarrantyWallet dashboard](docs/images/warrantywallet-dashboard.png)

The dashboard groups saved products by warranty status so it is easy to see what is covered, what is nearing expiry, and what has expired.

## Features

- **Product register:** Save a product's name, category, brand, purchase date, warranty duration, serial number, seller, and notes.
- **Automatic expiry dates:** Calculate the warranty end date from the purchase date and warranty duration.
- **Status dashboard:** View products that are in cover, ending within 30 days, or expired.
- **Receipt attachments:** Upload PDF, JPG, or PNG purchase receipts (maximum 8 MB per file).
- **Product records:** Open a product detail page to review its saved information and receipt.
- **Repair history:** Record service dates, service centres, repair details, and costs.
- **In-app reminders:** A daily scheduled task creates reminders for warranties expiring within 30 days and products that expired within the previous 7 days.
- **Related-record cleanup:** Deleting a product also removes its associated repair and reminder records.
- **Database options:** Use H2 for local development and PostgreSQL configuration for deployment.

## Technology stack

| Area | Technology |
|---|---|
| Language | Java 21 |
| Application framework | Spring Boot 3.5.7 |
| Web layer | Spring MVC, Thymeleaf |
| Persistence | Spring Data JPA, Hibernate |
| Databases | H2 (local), PostgreSQL (deployment configuration) |
| Validation | Jakarta Bean Validation |
| Scheduled tasks | Spring Scheduler |
| Tests | JUnit 5 |
| Build | Maven |
| Containerisation | Docker |

## Run locally

### Prerequisites

- JDK 21
- Maven 3.9 or later

### Windows

1. Clone the repository:

   ```bash
   git clone https://github.com/codewithsayan7/WarrantyWallet.git
   ```

2. Open the project folder that contains `pom.xml` in VS Code.
3. Start the application:

   ```bash
   mvn org.springframework.boot:spring-boot-maven-plugin:3.5.7:run
   ```

4. Open [http://localhost:8080](http://localhost:8080).

You can also use the included `RUN-WINDOWS.bat` file from the project root.

## Local data and uploaded receipts

- The local H2 database is stored in `warrantywalletdb.mv.db`.
- Uploaded receipts are stored under `uploads/receipts/`.
- These are local runtime data and are excluded from Git by `.gitignore`.

Back up the database and receipt directory before deleting or moving the local project folder.

## Scheduled reminder behaviour

The scheduler runs every day at **8:00 AM in the server's local timezone**. It creates one in-app reminder per product per day when a warranty is due to expire within the next 30 days or expired within the previous 7 days.

Reminders are displayed inside the application. **Email, SMS, and push notifications are not implemented.** A missed scheduled run is not replayed automatically.

## PostgreSQL deployment configuration

The application supports the following environment variables for a PostgreSQL deployment:

| Variable | Purpose |
|---|---|
| `DB_URL` | JDBC connection URL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `DB_DRIVER` | Set to `org.postgresql.Driver` |
| `PORT` | Hosting platform's application port |
| `UPLOAD_DIR` | Directory used to store uploaded receipts |

Configure these values in the hosting provider's environment settings. Do not commit credentials or `.env` files.

**Persistent storage matters:** receipt files are stored on the application filesystem. On hosts with ephemeral filesystems, uploads may disappear when an instance restarts or is replaced. Use a persistent disk or object storage (for example, S3) before relying on receipt uploads in production.

## Tests

Run the test suite with:

```bash
mvn test
```

## Implementation topics

Areas of the codebase worth exploring:

- Warranty expiry-date calculations using `LocalDate`.
- JPA entities and repository relationships for products, repairs, and reminders.
- Form validation and receipt upload handling.
- Safe receipt filename generation and protected file retrieval.
- Scheduled reminder generation and duplicate prevention.
- H2 local development versus PostgreSQL deployment configuration.

## Planned improvements

These are possible future enhancements, not current features:

- User accounts and household-level ownership.
- Cloud receipt storage using S3 or a similar object store.
- Email or push notifications.
- Reminder retry and delivery status.
- Audit history and expanded integration tests.

## Project structure

```text
WarrantyWallet/
├── src/
│   ├── main/
│   │   ├── java/              # Controllers, entities, services and repositories
│   │   └── resources/
│   │       ├── static/        # CSS and static assets
│   │       └── templates/     # Thymeleaf pages
│   └── test/                  # Automated tests
├── docs/
│   └── images/                # README screenshots
├── Dockerfile
├── pom.xml
└── README.md
```

## License

No license has been added yet. Contact the repository owner before reusing or distributing this project.
