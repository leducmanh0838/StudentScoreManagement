# 🎓 Student Grade Management System

---

## Features

- 🧑‍🎓 **Students**: View personal results, register for the course.
- 👨‍🏫 **Teachers**: Add, edit, and manage student grades
- 📊 **Academic** staff: Manage teachers, course, course session, view statistics and generate reports

## Tech Stack

### Backend
- **Spring MVC**: A framework for building web applications using the Model-View-Controller architecture.
- **Spring Security**: A library for securing applications, handling authentication and authorization.
- **Spring Mail**: Provides email sending capabilities within Spring applications.
- **Hibernate**: An ORM library that simplifies database interactions with Java objects.
- **MySQL**: A relational database management system used for data storage.
- **Cloudinary**:  A cloud-based service for managing and hosting images and videos.
- **Google OAuth 2.0**: A secure user authentication method allowing login with Google accounts.
### Frontend
- **ReactJS**: A JavaScript library for building dynamic and responsive user interfaces.
- **React Boostrap**: A UI toolkit based on Bootstrap that simplifies building React components with pre-styled elements.
- **Firebase**: Used specifically for real-time chat functionality and messaging services.
---

## Pages

### Student
**Home Page**:

<img width="900" height="500" alt="image" src="https://github.com/user-attachments/assets/bc7070b9-3352-477d-9c71-e1e8362d9494" />

**Login**:

<img width="1897" height="1023" alt="image" src="https://github.com/user-attachments/assets/f05a7c2a-350a-4866-b85f-adf14201817c" />

**Account Registration**:

<img width="1894" height="1033" alt="image" src="https://github.com/user-attachments/assets/8c734a54-600f-4e37-b6d0-eba5da9a6390" />

**OTP authentication**:

<img width="1893" height="1025" alt="image" src="https://github.com/user-attachments/assets/fea8fb3d-8fec-4241-be81-0c181ea06361" />

<img width="1513" height="828" alt="image" src="https://github.com/user-attachments/assets/99edd4ff-f459-4df0-93c6-7dad72aa0bc1" />

**Course Registration**

<img width="1919" height="1016" alt="image" src="https://github.com/user-attachments/assets/64455852-3971-468c-952f-6bbf0d1116b9" />

**List Of Registered Subjects**

<img width="1919" height="1029" alt="image" src="https://github.com/user-attachments/assets/140402ee-78af-4cca-b522-d40c0c2202ac" />

**Realtime chat**

<img width="1881" height="1081" alt="image" src="https://github.com/user-attachments/assets/6b4b3cb6-30d1-4a92-bc2d-195e513e106d" />

### Teacher
**Grade management**

<img width="1919" height="1080" alt="image" src="https://github.com/user-attachments/assets/834bf524-5719-4855-b18d-7597fb12ffc5" />

**Criteria management**

<img width="1919" height="510" alt="image" src="https://github.com/user-attachments/assets/84fe258a-6540-40b9-88b7-71ce02778eef" />


### Academic staff
**Home Page**:

<img width="1919" height="1041" alt="image" src="https://github.com/user-attachments/assets/43bbb60f-0280-46e4-b980-58c2a31a8756" />

**Teacher Management**

<img width="1914" height="1083" alt="image" src="https://github.com/user-attachments/assets/dfdb9ee0-c948-42cb-9116-fe90640311fb" />

**Course Management**

<img width="1919" height="1027" alt="image" src="https://github.com/user-attachments/assets/cd894162-9da0-4844-a947-f5b67d9d93d1" />

**Course Session Management**

<img width="1919" height="1032" alt="image" src="https://github.com/user-attachments/assets/5d178277-ad55-421e-b7bf-1f092e977be5" />

**Statistical**

<img width="1919" height="1071" alt="image" src="https://github.com/user-attachments/assets/0e3ee9be-d3e6-4e8d-92dc-56138dc648e7" />

<img width="1895" height="1081" alt="image" src="https://github.com/user-attachments/assets/6448cd5a-5a14-475f-848f-afbaa558ad16" />


## Database
<img width="1249" height="943" alt="image" src="https://github.com/user-attachments/assets/67040ee0-d631-442d-a921-08a6c7c8fd59" />

## Installation & Database Setup

**Step 1 — Clone project**
```bash
git clone https://github.com/leducmanh0838/StudentScoreManagement.git
```

**Step 2 — Create table and data**
- Create your mysql database
- Run CreateTableFile.sql to create tables
- Run CreateDataFile.sql to insert data
<img width="768" height="81" alt="image" src="https://github.com/user-attachments/assets/fbe37adc-d629-4dca-a2b8-b809c6f8fe39" />

**Step 3 — Configure environment variables**
- front-end/.env
```bash
REACT_APP_GOOGLE_CLIENT_ID=<your_google_client_id_here>

REACT_APP_FIREBASE_API_KEY=<your_firebase_api_key_here>
REACT_APP_FIREBASE_AUTH_DOMAIN=<your_firebase_auth_domain_here>
REACT_APP_FIREBASE_PROJECT_ID=<your_firebase_project_id_here>
REACT_APP_FIREBASE_STORAGE_BUCKET=<your_firebase_storage_bucket_here>
REACT_APP_FIREBASE_MESSAGING_SENDER_ID=<your_firebase_messaging_sender_id_here>
REACT_APP_FIREBASE_APP_ID=<your_firebase_app_id_here>
REACT_APP_FIREBASE_MEASUREMENT_ID=<your_firebase_measurement_id_here>
```

- Spring/src/main/resources/application.properties
```bash
# ===============================
# Hibernate Configuration
# ===============================
hibernate.dialect=<your_hibernate_dialect>
hibernate.showSql=<true_or_false>
hibernate.connection.driverClass=<your_jdbc_driver_class>
hibernate.connection.url=<your_database_url>
hibernate.connection.username=<your_database_username>
hibernate.connection.password=<your_database_password>

# ===============================
# Cloudinary Configuration
# ===============================
cloudinary.cloud_name=<your_cloudinary_cloud_name>
cloudinary.api_key=<your_cloudinary_api_key>
cloudinary.api_secret=<your_cloudinary_api_secret>
cloudinary.secure=<true_or_false>

# ===============================
# Email Configuration
# ===============================
mail.username=<your_email_username>
mail.password=<your_email_password>

# ===============================
# Google OAuth 2.0
# ===============================
google.client-id=<your_google_client_id>
```
