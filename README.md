# WarrantyWallet

A household product warranty and maintenance organiser built with Java and Spring Boot.

## Features
- Register products with category, brand, purchase date and warranty duration.
- Calculate expiry dates automatically.
- Dashboard for covered, expiring-soon and expired products.
- Upload PDF/JPG/PNG purchase receipts (maximum 8 MB).
- Product detail page with serial number, store and notes.
- Repair/service history with date, service centre and cost.
- Daily scheduled in-app reminders for products expiring within 30 days or expired within the last 7 days.
- Delete product and its related repair/reminder records.
- Persistent H2 local database and PostgreSQL deployment configuration.

## Stack
Java 21, Spring Boot 3.5.7, Spring MVC, Thymeleaf, Spring Data JPA, Hibernate, H2, PostgreSQL, Jakarta Validation, Spring Scheduler, JUnit 5, Maven, Docker.

## Run on Windows
1. Install JDK 21 and Maven 3.9+.
2. Extract the ZIP and open the `warrantywallet` folder in VS Code (the folder containing `pom.xml`).
3. Open Terminal -> New Terminal.
4. Run:
   `mvn org.springframework.boot:spring-boot-maven-plugin:3.5.7:run`
5. Visit `http://localhost:8080`.

The persistent local H2 database is stored as `warrantywalletdb.mv.db`. Receipts are stored under `uploads/receipts/`.

## Demo records
Three illustrative sample products are inserted on first startup and marked Sample. They are not actual purchases. Add your own records to customise the app.

## Scheduled reminders
The scheduler runs daily at 8:00 AM in the server's local timezone. It creates one in-app reminder per product per day for warranties expiring within 30 days or which expired in the previous 7 days. No email, SMS or push notification is included. A missed run is not replayed automatically.

## PostgreSQL deployment
Set `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `DB_DRIVER=org.postgresql.Driver` and `PORT` in your host. Set `UPLOAD_DIR` to durable writable storage if receipt uploads must survive restarts. Never commit credentials. Local receipt storage is not suitable for ephemeral hosting; use persistent storage or object storage such as S3 for production.

## Resume readiness
Before describing this project on your resume, test the workflows and be able to explain:
- Java LocalDate expiry calculations.
- JPA relationships and repair history.
- Validation and safe receipt filename generation.
- Path traversal protection in receipt retrieval.
- Scheduled duplicate-prevention for reminders.
- H2 local persistence vs PostgreSQL deployment.

## Next improvements
Authentication and household ownership, S3 receipt storage, email notifications, reminder retry status, audit history, and more integration tests.


## Interface design

The UI uses a restrained household-register visual style: warm paper background, compact product rows, plain status labels and system/Georgia typography. The main design tokens are at the top of `src/main/resources/static/css/app.css`. You can make the interface more personal by adjusting the accent colour, category labels, page copy and empty-state wording to match the household workflow you want to support.
