# 🏠 TokenVest - Real Estate Token Investment Platform

## 📌 Problem Statement

Real estate investment has traditionally been capital-intensive, making it inaccessible for individuals with limited funds. TokenVest aims to solve this problem by introducing **tokenized property investment**. Instead of purchasing an entire property, users can buy **tokens**—each representing a share of the property—making real estate investment more **affordable, flexible, and inclusive**.

---

## 💡 Solution Overview

**TokenVest** is a Java-based investment platform that brings the concept of **fractional ownership** into the real estate space. By dividing properties into tokens, users can invest small amounts and own a share in premium real estate projects. The system supports two types of users: **Admin** and **Investor**, each with specific roles and access rights.

The project is implemented using core Java with a **console interface** (extendable to a Swing GUI) and uses **in-memory data structures** like ArrayList and HashMap for storing and managing application data.

---

## 👤 User Roles & Functionalities

### 🛠️ Admin Features

- Add new properties
- View properties
- Update or delete property details
- Update KYC status
- View investors
- View analytics of:
  - Most tokens sold 
  - Properties generating highest revenue
  - Properties that are sold out
  - KYC requests
- Manage and respond to support queries from investors
- Update password


### 💼 Investor Features

- View all available properties
- Purchase tokens of selected properties
- View owned tokens and transaction history
- Resell previously purchased tokens
- Purchase tokens listed by other investors
- Update password
- Submit queries or concerns to admin

---
