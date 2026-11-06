# WarrantyWallet

WarrantyWallet helps you keep track of the products you buy, their warranty dates, receipts, and repair history. Everything is organised in one simple household register.

## Dashboard

![WarrantyWallet dashboard](docs/images/warrantywallet-dashboard.png)

## What you can do

- Add products and save their purchase details.
- Automatically calculate warranty end dates.
- See which warranties are active, ending soon, or expired.
- Upload purchase receipts.
- Keep a record of repairs and service visits.
- Get in-app reminders about warranties that are ending soon or have recently expired.
- View all your saved products from one dashboard.

## Built with

- Java 21
- Spring Boot
- Thymeleaf
- HTML and CSS
- H2 database for local use
- PostgreSQL for the hosted version

## Run it on your computer

**You need:** Java 21 and Maven installed.

1. Download or clone this repository:

   ```bash
   git clone https://github.com/codewithsayan7/WarrantyWallet.git
   ```

2. Open the `WarrantyWallet` folder in VS Code.
3. Open the terminal in that folder.
4. Run:

   ```bash
   mvn org.springframework.boot:spring-boot-maven-plugin:3.5.7:run
   ```

5. Open [http://localhost:8080](http://localhost:8080) in your browser.

On Windows, you can also double-click `RUN-WINDOWS.bat` to start the app.

## Demo data

The app includes a few sample products so you can explore the dashboard. They are only examples, not real purchases.

## A note about receipts

Receipts are saved as files separately from product information. On the hosted free service, uploaded receipts may not remain available after a restart or redeployment. The database stores the product and repair records.

## Project

Created as a Java and Spring Boot portfolio project.

[View the source code on GitHub](https://github.com/codewithsayan7/WarrantyWallet)
