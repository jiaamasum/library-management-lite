# Library Management Lite

Simple, file‑based Library Management System written in Java 17 with a clean domain model and a small console UI. It stores data in CSV files so you can run it without any database.

## Features
- Manage books and members (create/list/search)
- Borrow/return (with availability tracking)
- Persist data to CSV files in the `data/` folder
- Fast CLI workflow with a simple menu

## Tech Stack
- Java 17
- Maven (build)

## Project Layout
```
src/main/java/
  exceptions/                 # Typed exceptions for domain rules
  thelibrarysystem/
    App.java                  # Console entrypoint
    Library.java              # Core service: CRUD, checkout/return, load/save
    FileStorage.java          # CSV persistence utilities
    Book.java, Member.java    # Models
    Loan.java, Person.java    # Models
    Identifiable.java, CsvSerializable.java
```

## Quick Start
1) Ensure Java 17 is installed and on PATH (`java -version`).
2) Build the project:
```
mvn clean package
```
3) Run the app:
```
java -cp target/LibraryManagementSystem-1.0-SNAPSHOT.jar thelibrarysystem.App
```

The app will create CSV files in `data/` on first save. On first run it seeds two example books and two members for quick testing.

## Usage
Menu options:
- 1 Add Book
- 2 Register Member
- 3 List Books (sorted)
- 4 Search Books
- 5 Checkout (enter Book ID, then Member ID)
- 6 Return Book (enter Loan ID printed on checkout)
- 0 Exit (saves data)

Seeded IDs (first run only):
- Books: `B-100` (Effective Java), `B-200` (Clean Code)
- Members: `M-1`, `M-2`

## Development
- Java 17 code style, small focused classes
- Exceptions model domain failures (`AlreadyExists`, `NotFound`, `NotAvailable`)
- CSV helpers kept simple and explicit

### Run from IDE (IntelliJ)
1) Set Project SDK to 17 and language level 17.
2) Right‑click `thelibrarysystem.App` → Run.

## Git Flow in This Repo
- `release`: integration/backup branch where full feature commits land first
- `main`: stable default branch. Initially contains only this README, then merges from `release`.

Commit categories used:
- `chore(build)`: Maven and gitignore
- `feat(domain)`: core models and interfaces
- `feat(exceptions)`: typed exceptions
- `feat(storage)`: CSV persistence
- `feat(service)`: library service
- `feat(cli)`: console UI
- `chore(demo)`: non‑essential example code

---

Maintained by @jiaamasum
