# WayFinder 🗺️

## Overview
WayFinder is a **safety-focused location sharing application** designed to help users stay connected with friends and family through secure, real-time location tracking. Built as a Computer Networks course project, it demonstrates modern web application architecture with emphasis on user privacy and safety.

## What is WayFinder?
WayFinder allows users to:
- **Share location safely** with trusted friends and family
- **Send and manage friend requests** with a curated friend limit (max 10 friends)
- **Track real-time locations** of connected friends for safety purposes
- **Maintain privacy** through controlled friend networks and secure authentication

## Key Features
- 🔒 **Secure Authentication** - Password-based user verification
- 👥 **Friend Network Management** - Send, accept, and manage friend requests
- 📍 **Real-time Location Sharing** - Share and view friend locations
- 🛡️ **Privacy Controls** - Limited friend networks ensure safety
- 🌐 **Cross-platform Access** - Web-based application accessible anywhere

## Technology Stack
- **Frontend**: Vaadin (Java-based web UI framework)
- **Backend**: Spring Boot REST API
- **Database**: MongoDB Atlas (cloud-hosted)
- **Architecture**: Three-tier system with clean separation of concerns

## Project Structure
```
WayFinder/
├── frontend/          # Vaadin web interface
├── backend/           # Spring Boot REST API
├── mongoDB/           # Database utilities and testing
└── README.md          # This file
```

## Getting Started
1. **Backend**: `cd backend && mvn spring-boot:run` (runs on port 8067)
2. **Frontend**: `cd frontend && mvn spring-boot:run` (Vaadin dev mode)
3. **Environment**: Ensure `.env` files are configured with MongoDB connection strings

## Safety & Privacy
WayFinder prioritizes user safety through:
- **Limited friend networks** (maximum 10 friends per user)
- **Explicit consent** required for location sharing
- **No public location data** - only shared with confirmed friends
- **Secure data storage** in cloud-hosted MongoDB

## Contributors
- Akshat Patiyal (2023062)
- Chirag Sehgal (2023176)
- Dhruv Jaiswal (2023200)
- Jasjyot Singh Gulati (2023267)
- Sushen Tentiwal (2023259)
---
*A Computer Networks project demonstrating modern web application architecture, real-time data synchronization, and secure user management.*
