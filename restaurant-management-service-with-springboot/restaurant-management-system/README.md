# 🍽️ Restaurant Management System

A **Spring Boot** application designed to handle restaurant operations such as **menu item management**, **order handling**, **table reservations**, and various **admin functionalities**.

---

## 🚀 Technologies Utilized

- **Java 17+**
- **Spring Boot**
- **Spring Security** with **JWT-based Authentication**
- **Maven**
- **PostgreSQL** (easily configurable for other databases)
- **Hibernate / JPA**

---

## ✨ Core Features

- **User Authentication & Role-Based Access**
  - Roles include: `ADMIN`, `MANAGER`, `WAITER`, `CHEF`, `CUSTOMER`
- **Menu Management**
  - Create, modify, remove, and view menu items
- **Order Management**
  - Initiate orders, modify order items, update order status
- **Table Reservation System**
  - Reserve, update, or cancel table bookings
- **Billing**
  - Generate bills after order completion

---

## 📂 Project Layout

graph TD
    A[restaurant-management-system] --> B[src/main/java/com/zeta_training/restaurant_management_system]
    B --> B1[config - Security and app setup]
    B --> B2[controller - API endpoints]
    B --> B3[dto - Data Transfer Objects]
    B --> B4[entity - JPA Entities]
    B --> B5[enumeration - Enums]
    B --> B6[exception - Custom exceptions]
    B --> B7[repository - Interfaces]
    B --> B8[service - Business logic]
    B --> B9[util - JWT and utilities]
    A --> C[src/main/resources - Config and static files]


⚙️ Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

▶️ Build and Launch
mvn clean install
mvn spring-boot:run

🔐 Role-Based Access Overview
| Role     | Permissions                                         |
| -------- | --------------------------------------------------- |
| ADMIN    | Register employees, manage bookings, generate bills |
| MANAGER  | Oversee bookings and menu entries                   |
| WAITER   | Submit and manage orders                            |
| CHEF     | Update status of order items                        |
| CUSTOMER | Make table reservations                             |

📌 API Overview
| Method   | Endpoint              | Payload       | Description                       |
| -------- | --------------------- | ------------- | --------------------------------- |
| **POST** | `/auth/registerAdmin` | `RegisterDTO` | Register a new admin user         |
| **GET**  | `/auth/login`         | `LoginDTO`    | Authenticate and obtain JWT token |

👨‍💼 Admin APIs (/admin)'
| Method   | Endpoint                            | Payload       | Description                                                     |
| -------- | ----------------------------------- | ------------- | --------------------------------------------------------------- |
| **POST** | `/admin/registerRestaurantEmployee` | `RegisterDTO` | Add new restaurant staff (e.g., waiter, chef, manager)          |
| **POST** | `/admin/generateBill/{orderId}`     | —             | Create a bill for a completed order. Returns `BillResponseDTO`. |

🧾 Menu Item APIs (/menuItem)
| Method     | Endpoint                        | Payload       | Description                         |
| ---------- | ------------------------------- | ------------- | ----------------------------------- |
| **POST**   | `/menuItem/addMenuItem`         | `MenuItemDTO` | Create a new menu item              |
| **GET**    | `/menuItem/getAllMenuItems`     | —             | Retrieve the list of all menu items |
| **DELETE** | `/menuItem/deleteMenuItem/{id}` | —             | Remove a menu item using its ID     |

🍽️ Order APIs (/orders)
| Method   | Endpoint                                | Payload        | Description                                                            |
| -------- | --------------------------------------- | -------------- | ---------------------------------------------------------------------- |
| **POST** | `/orders/place`                         | `OrderRequest` | Submit a new order or update an existing one                           |
| **PUT**  | `/orders/updateOrderItem/{orderItemId}` | `StatusDTO`    | Change the status of a specific order item (e.g., PENDING → COMPLETED) |

📅 Table Booking APIs (/table-booking)
| Method     | Endpoint                           | Payload           | Description                                            |
| ---------- | ---------------------------------- | ----------------- | ------------------------------------------------------ |
| **POST**   | `/table-booking/book`              | `TableBookingDTO` | Reserve a table. Returns booking ID                    |
| **GET**    | `/table-booking/getAll`            | —                 | Fetch all table reservations                           |
| **GET**    | `/table-booking/get/{id}`          | —                 | Retrieve booking by ID                                 |
| **PUT**    | `/table-booking/update/{id}`       | `TableBookingDTO` | Modify booking details by ID                           |
| **DELETE** | `/table-booking/delete/{id}`       | —                 | Remove a reservation by ID                             |
| **PUT**    | `/table-booking/updateStatus/{id}` | `StatusDTO`       | Update the booking status (e.g., CONFIRMED, CANCELLED) |
