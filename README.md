# SplitEase

SplitEase is a full-stack web app for splitting group expenses — think trips, roommates, or any shared costs. Create a group, add members, log expenses, and SplitEase automatically calculates who owes whom.

## Features

- **User authentication** — secure registration and login with Spring Security (BCrypt password hashing)
- **Groups** — create groups and add members to split expenses with
- **Expenses** — log shared expenses within a group
- **Balances** — automatic calculation of who owes whom based on logged expenses
- **Settlements** — record payments between members to settle up balances
- **Profile pictures** — upload and display a profile picture
- **Email notifications** — optional email alerts (skipped gracefully if not configured)

## Tech Stack

- **Backend:** Java, Spring Boot, Spring Security, Spring Data JPA (Hibernate)
- **Frontend:** Thymeleaf templates, HTML/CSS
- **Database:** MySQL
- **Build tool:** Maven

## Project Structure
## Setup & Running Locally

### Prerequisites
- Java 17+
- Maven
- MySQL running locally

### Steps

1. **Clone the repo**
```bash
   git clone https://github.com/kirankoduri11/SplitEase.git
   cd SplitEase
```

2. **Create the database**
```sql
   CREATE DATABASE expense_splitter;
```

3. **Configure your database credentials**

   Copy the example config and fill in your own MySQL username/password:
```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
```
   Then edit `src/main/resources/application.properties` and set `spring.datasource.password` to your actual MySQL password.

4. **Run the app**
```bash
   mvn spring-boot:run
```

5. **Open in browser**

   Go to `http://localhost:8080/register` to create an account, then log in.

## Notes

- `application.properties` (with real credentials) is gitignored and never committed — use the `.example` file as a template.
- Email sending is optional; the app runs fine without Gmail credentials configured.
