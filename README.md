Online Quiz Application
A full-featured **Java Swing + MySQL** project that lets users take quizzes and admins create/manage them — with login authentication, password security, and score calculation.
Features
1. **User Authentication** — Secure login system using hashed passwords  
1. **Admin Dashboard** — Create, view, and manage quizzes
3.**Quiz Management** — Add multiple-choice questions with options
4.**Quiz Taking System** — Users can attempt quizzes and view scores instantly  
5. **Score Calculation** — Auto-computes marks based on correct answers  
6. **MySQL Integration** — Data persistence with JDBC  
7. **Simple GUI** — Built using Java Swing for easy navigation  

## 🛠️ Tech Stack

| Layer | Technology |
|--------|-------------|
| **Frontend** | Java Swing |
| **Backend** | Core Java (OOP + JDBC) |
| **Database** | MySQL |
| **Security** | PBKDF2 Password Hashing |
| **IDE** | IntelliJ IDEA |
| **Build Tool** | Maven |

---

## ⚙️ Setup & Installation

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/bhanuperuri/OnlineQuizApplication.git
cd OnlineQuizApplication
````

### 2️⃣ Create MySQL Database

Open MySQL Workbench or CLI and run:

```sql
CREATE DATABASE quizapp CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'quizuser'@'localhost' IDENTIFIED BY 'quizpass';
GRANT ALL PRIVILEGES ON quizapp.* TO 'quizuser'@'localhost';
FLUSH PRIVILEGES;
```

### 3️⃣ Update `DBHelper.java` (if needed)

Make sure your MySQL credentials match:

```java
private static final String URL = "jdbc:mysql://localhost:3306/quizapp?useSSL=false";
private static final String USER = "quizuser";
private static final String PASSWORD = "quizpass";
```

### 4️⃣ Run the Project

In IntelliJ or terminal:

```bash
mvn clean compile
```

Then run the main class:

```
com.quizapp.Main
```

---

## 👨‍💼 Default Admin Login

| Username | Password           |
| -------- | ------------------ |
| `admin`  | `Bhanu@7842805122` |

You can change this later in `Main.java` or via MySQL.

---

## 🧩 Project Structure

```
OnlineQuizApplication/
│
├── src/main/java/com/quizapp/
│   ├── Main.java
│   ├── DBHelper.java
│   ├── User.java
│   ├── UserDAO.java
│   ├── PasswordUtil.java
│   ├── AdminFrame.java
│   ├── QuizListFrame.java
│   ├── QuizDAO.java
│   ├── TakeQuizFrame.java
│   ├── Quiz.java
│   ├── Question.java
│   └── Choice.java
│
├── pom.xml
└── README.md
```

---

## 🚀 Future Enhancements

* 🏆 Leaderboard for top scorers
* ⏱️ Timed quizzes
* 🧾 User registration system
* 🎨 Improved UI with better themes


## 🙌 Author

👩‍💻 **Bhanu Sri**
📧 [internship.innobyteservices@gmail.com](mailto:internship.innobyteservices@gmail.com)
💼 GitHub: [bhanuperuri](https://github.com/bhanuperuri)


### ⭐ If you like this project, give it a star on GitHub!


### 🧾 How to Add It
1. In your project folder, create a new file called **`README.md`**.
2. Paste the above markdown.
3. Save it.
4. In your terminal, run:
   ```bash
   git add README.md
   git commit -m "Added project README"
   git push
