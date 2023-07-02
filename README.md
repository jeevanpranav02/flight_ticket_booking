# Airplane Ticket Booking System

This project is an Airplane Ticket Booking System that allows users to search for flights, book tickets, and manage their bookings. It also provides administrative features for admins to manage flights and view bookings.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)

## Features

### User Features

- [x] **Login**: Users can authenticate themselves into the system.
- [x] **Sign up**: Users can create a new account with appropriate data validations.
- [ ] **Search for Flights**: Users can search for flights based on specific date and time criteria.
- [ ] **Book Tickets**: Users can book tickets on a flight based on availability of seats.
- [ ] **My Booking**: Users can view a list of all the bookings made by them.
- [ ] **Logout**: Users can log out from the system.

### Admin Features

- [x] **Admin Login**: Administrators can authenticate themselves into the system with administrative privileges.
- [x] **Admin Sign up**: Administrators can create a new account with appropriate data validations.
- [ ] **Add Flights**: Administrators can add new flights to the system, providing flight details such as airline, departure, and arrival information.
- [ ] **Remove Flights**: Administrators can remove flights from the system based on flight number or other criteria.
- [ ] **View Bookings**: Administrators can view all the bookings for a specific flight based on flight number and time.
- [ ] **Logout**: Administrators can log out from the system.

## Technologies Used

- Backend: Java, Spring Boot
- Database: PostgreSQL, Hibernate
- Version Control: Git, GitHub

## Getting Started

To get started with the Airplane Ticket Booking System, follow the steps below:

1. Clone the GitHub repository:

   ```bash
   git clone https://github.com/your-username/airplane-ticket-booking.git
    ```
2. Install PostgreSQL and create a database.

3. Configure the database connection in the application.yml file.

4. Import the required Maven dependencies by running the following command:
   ```bash
   mvn clean install
   ```
5. Run the application using the following command:
   ```bash
   mvn spring-boot:run
    ```
   
Note: This application is still under development. To test it, use a REST client like Postman or Insomnia.
