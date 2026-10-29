# Employee Management System - Java

Tech: Java, Swing, JDBC, MySQL, Maven.

## Setup
1. Create the database by running `database.sql` in MySQL Workbench.
2. Open `src/main/java/com/ems/util/DBConnection.java`.
3. Change `YOUR_MYSQL_PASSWORD` to your MySQL root password.
4. Open the project in VS Code/Eclipse as a Maven project.
5. Run `com.ems.Main` or `mvn exec:java`.

Default login:
- Username: `admin`
- Password: `admin123`

Features: login, add, view, search, update, delete employees; JDBC/MySQL integration; Swing GUI; prepared statements; SHA-256 password hashing.
