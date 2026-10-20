
# Technical University of Moldova

## Faculty of Computers, Informatics and Microelectronics

### Laboratory Work No. 1

**Subject:** Tehnici și Mecanisme de Proiectare Software (TMPS)  
**Project:** Online Store Application  
**Mentor:** asist. univ. Istrati Daniel  
**Student:** Cuibari Vladislav  
**Group:** FAF-243

---

## Table of Contents

## Table of Contents

1. Introduction and Objectives
2. Application Description
3. Project Architecture
4. Implementation Details
5. SOLID Principles
6. Testing and Validation
7. Limitations and Future Development
8. Conclusion

---

## 1. Introduction

This laboratory work presents the implementation of a simple online-store application written in Java. The project was created as a foundation that can be gradually extended throughout the semester with new functionality and software design techniques.

The application models a basic purchasing process. Products are created and added to a shopping cart, the cart subtotal is calculated, a discount policy is selected, checkout is performed, and the resulting order is stored in memory.

The main purpose is not to reproduce a complete commercial platform. Instead, the project demonstrates how a relatively small program can be divided into focused and extensible components by applying three SOLID principles:

- Single Responsibility Principle;

- Open/Closed Principle;

- Dependency Inversion Principle.

The implementation intentionally avoids unnecessary functionality such as real payment processing, authentication, networking. This keeps the first version understandable while preserving the possibility of future development.

## 2. Purpose of the Laboratory Work

The purpose of this laboratory work is to study and apply object-oriented design principles in a practical Java application.

The project demonstrates how responsibilities can be separated between classes, how new behavior can be added without modifying stable business logic, and how high-level services can depend on abstractions instead of specific implementations.

The laboratory work also develops practical knowledge of:

## 3. Project Objectives

The main objectives are:

1. Create a small online-store domain using Java classes.
2. Separate product, cart, discount, checkout, and storage responsibilities.
3. Implement at least three SOLID principles.
4. Make discount behavior extensible.
5. Separate order storage from checkout logic.
6. Validate incorrect data such as negative prices or quantities.
7. Keep the application simple enough to understand and demonstrate.
8. Prepare the architecture for future semester requirements.

## 4. Application Description

The application simulates the following scenario:

1. Three products are created: a mechanical keyboard and a wireless mouse.
2. A new shopping cart is created.
3. Products and their quantities are added to the cart.
4. The subtotal is calculated from the price and quantity of each item.
5. A percentage discount is selected.
6. The checkout service validates the input.
7. A new order with a unique identifier is created.
8. The order is saved using an in-memory repository.
9. The cart is cleared after a successful checkout.
10. The order information is displayed to the user.

## Conclusion

In this laboratory work, I developed a simple online-store application in Java and applied three SOLID principles: **Single Responsibility**, **Open/Closed**, and **Dependency Inversion**.

The application separates product management, cart operations, discount calculation, checkout, and order storage into focused components. This structure makes the code easier to understand, maintain, test, and extend.

New discount types can be introduced without changing the checkout logic, while the in-memory repository can later be replaced with file or database storage. The project provides a clear foundation for implementing additional functionality and design patterns throughout the semester.