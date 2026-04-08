# GearRent Pro – Multi-Branch Equipment Rental System

Java Swing desktop application built with layered architecture and MySQL.

## Tech Stack
- Java (Ant project style for NetBeans)
- Java Swing (UI)
- MySQL
- JDBC

## Layered Architecture
- **Presentation Layer**: `edu.ijse.lk.ui`
- **Controller Layer**: `edu.ijse.lk.controller`
- **Service/Business Layer**: `edu.ijse.lk.service`
- **DAO/Repository Layer**: `edu.ijse.lk.dao`, `edu.ijse.lk.dao.impl`
- **Entity Layer**: `edu.ijse.lk.entity`
- **DTO Layer**: `edu.ijse.lk.dto`
- **DB Singleton**: `edu.ijse.lk.util.DBConnection`

## Modules Implemented
- Branch
- Equipment Category
- Equipment
- Customer
- Membership
- Reservation
- Rental
- User (Login)

## Key Features
- Role-based login (Admin / BranchManager / Staff)
- Branch CRUD (Admin)
- Category CRUD and activation/deactivation (Admin)
- Equipment management with branch/category/status filters
- Customer management with optional membership
- Reservation creation with overlap prevention and max 30-day validation
- Rental creation (direct or from reservation) with pricing, long-rental discount, membership discount
- Return and settlement (late fee, damage charge, deposit offset)
- Overdue rental listing and status update
- Reports:
  - Branch-wise revenue report (date range)
  - Equipment utilization report (rental days vs available days)
- Transaction handling for critical flows:
  - Reservation create
  - Reservation convert to rental
  - Rental create
  - Rental return

## Project Structure
- `GearRentPro/src` Java source
- `SQL_Script.sql` database queries
- `GearRentPro/build.xml` Ant build file
- `GearRentPro/nbproject` NetBeans project metadata

## Database Setup
1. Start MySQL server.
2. Run the queries in SQL_script.sql
3. Ensure DB is accessible at:
   - URL: `jdbc:mysql://localhost:3306/gearrent_pro?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`
   - User: `root`
   - Password: add the password to your mySQL DataBase


## Build and Run
From repository root:
```bash
ant clean
ant compile
ant run
```

## Default Login Credentials
- **Admin**: `admin / admin123`
- **Branch Manager**: `manager_panadura / 1234`, `manager_galle / 1234`
- **Staff**: `staff_panadura / 1234`, `staff_galle / 1234`

## Notes
- Add MySQL JDBC driver (Connector/J) to classpath in your IDE or Ant runtime.
