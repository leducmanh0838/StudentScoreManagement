USE wsddatabase;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS OTP_Verification;
DROP TABLE IF EXISTS Pre_Student_Registration;
DROP TABLE IF EXISTS Comment;
DROP TABLE IF EXISTS Forum_Post;
DROP TABLE IF EXISTS Notification;
DROP TABLE IF EXISTS Additional_Grade;
DROP TABLE IF EXISTS Grade;
DROP TABLE IF EXISTS Enrollment;
DROP TABLE IF EXISTS Course;
DROP TABLE IF EXISTS Course_Session;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Additional_Criteria;
DROP TABLE IF EXISTS Criteria;
DROP TABLE IF EXISTS Additional_Grade;
SET FOREIGN_KEY_CHECKS = 1;


-- CREATE TABLE OTP_Verification (
--     id INT AUTO_INCREMENT PRIMARY KEY,
--     email VARCHAR(255) NOT NULL,
--     otp VARCHAR(10) NOT NULL,
--     created_at DATETIME DEFAULT CURRENT_TIMESTAMP
-- );

CREATE TABLE Pre_Student_Registration (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    avatar VARCHAR(255),
    otp VARCHAR(6),  -- OTP code for verification
    otp_expiration DATETIME,  -- Time when OTP expires
    verification_attempts INT DEFAULT 0,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE User (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_code VARCHAR(20) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    role ENUM('admin', 'staff', 'teacher', 'student') NOT NULL,
    avatar VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    INDEX(email)
);


-- Bảng môn học
CREATE TABLE Course (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng buổi học của từng môn
CREATE TABLE Course_Session (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    course_id INT NOT NULL,
    teacher_id INT NOT NULL,
    is_open BOOLEAN DEFAULT TRUE,
    grade_status ENUM('Draft','Locked') DEFAULT 'Draft',
    is_active BOOLEAN DEFAULT TRUE,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES Course(id) ON DELETE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES User(id) ON DELETE RESTRICT
);

-- Thêm tiêu chí cột điểm
CREATE TABLE Criteria(
	id INT AUTO_INCREMENT PRIMARY KEY,
    criteria_name VARCHAR(20) NOT NULL,
    weight INT DEFAULT 30,
    course_session_id INT NOT NULL,
    FOREIGN KEY (course_session_id) REFERENCES Course_Session(id) ON DELETE CASCADE,
    UNIQUE (criteria_name, course_session_id)
);

-- Đăng ký
CREATE TABLE Enrollment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    course_session_id INT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE RESTRICT,
    FOREIGN KEY (course_session_id) REFERENCES Course_Session(id) ON DELETE RESTRICT,
    UNIQUE (user_id, course_session_id)
);

-- Điểm thêm
CREATE TABLE Grade(
    id INT AUTO_INCREMENT PRIMARY KEY,
    enrollment_id INT NOT NULL,
    criteria_id INT NOT NULL,
    score FLOAT NOT NULL,
    FOREIGN KEY (enrollment_id) REFERENCES Enrollment(id) ON DELETE CASCADE,
    FOREIGN KEY (criteria_id) REFERENCES Criteria(id) ON DELETE CASCADE,
    UNIQUE (enrollment_id, criteria_id)
);

CREATE TABLE Forum_Post (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    course_session_id INT,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE,
    FOREIGN KEY (course_session_id) REFERENCES Course_Session(id) ON DELETE CASCADE
);

CREATE TABLE Comment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    forum_post_id INT,
    user_id INT,
    content TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (forum_post_id) REFERENCES Forum_Post(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

-- CREATE TABLE Notification (
--     id INT AUTO_INCREMENT PRIMARY KEY,
--     user_id INT,
--     message TEXT NOT NULL,
--     is_read BOOLEAN DEFAULT FALSE,
--     is_active BOOLEAN DEFAULT TRUE,
--     created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
--     updated_date DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
--     FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
-- );