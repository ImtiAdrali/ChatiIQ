# 🚀 ChatiIQ — AI Chat Application (Spring Boot + Ollama)

ChatiIQ is a **production-style AI chatbot backend** built with Spring Boot and powered by a locally running LLM using Ollama.

It supports **chat history persistence, contextual memory, and chat lifecycle management**, making it a strong foundation for advanced features like RAG, streaming responses, and multi-user support.

---

## 🧠 Features

### ✅ Core Features

* 🤖 AI Chat using local LLM (via Ollama)
* 💾 Chat History Persistence (MySQL)
* 🧠 Contextual Memory (conversation-aware responses)
* 🗑️ Delete Conversation
* 🔄 Clear Chat History (reset messages only)

---

## 🏗️ Architecture

```
Client (React / Postman)
        ↓
Spring Boot API (ChatiIQ)
        ↓
LLM (Ollama - Local)
        ↓
MySQL Database
```

---

## 📦 Tech Stack

* **Backend:** Spring Boot
* **AI Integration:** Spring AI + Ollama
* **Database:** MySQL
* **ORM:** Spring Data JPA (Hibernate)
* **Build Tool:** Maven

---

## 📁 Project Structure

```
com.chatiiq
│
├── controller      # REST APIs
├── service         # Business logic
├── client          # LLM integration (Spring AI)
├── dto             # Request/Response models
├── config          # Configuration classes
└── entity          # JPA entities
```

---

## ⚙️ Setup Instructions

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/your-username/chatiiq.git
cd chatiiq
```

---

### 2️⃣ Run Ollama Locally

Install and start Ollama:

```bash
ollama run llama3
```

Default endpoint:

```
http://localhost:11434
```

---

### 3️⃣ Configure Database

Create a MySQL database:

```sql
CREATE DATABASE chatiiq;
```

---

### 4️⃣ Configure Environment Variables

You can use either `.env` or system environment variables:

#### Option A — Environment Variables (Recommended)

```bash
export DB_URL=jdbc:mysql://localhost:3306/chatiiq
export DB_USERNAME=root
export DB_PASSWORD=yourpassword
```

---

#### Option B — `.env` File

```
DB_URL=jdbc:mysql://localhost:3306/chatiiq
DB_USERNAME=root
DB_PASSWORD=yourpassword
```

---

### 5️⃣ application.yml

```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        options:
          model: llama3
```

---

### 6️⃣ Run the Application

```bash
mvn spring-boot:run
```

---

## 🔌 API Endpoints

### 🧠 Chat

```http
POST /api/chat
```

Request:

```json
{
  "message": "What is Spring Boot?"
}
```

Optional:

```
/api/chat?conversationId=1
```

---

### 🗑️ Delete Conversation

```http
DELETE /api/chat/{conversationId}
```

---

### 🔄 Clear Chat History

```http
DELETE /api/chat/{conversationId}/messages
```

---

## 🧠 How Memory Works

* Each conversation is stored in the database
* Messages are retrieved and sent back to the LLM
* The chatbot becomes **context-aware** by replaying previous messages

---

## ⚠️ Known Limitations

* Context window is limited (currently uses sliding window)
* No authentication (yet)
* No streaming responses (yet)

---

## 🚀 Future Enhancements

* 🔥 Streaming responses (real-time typing)
* 📄 File upload + RAG implementation
* 🔐 Authentication (JWT)
* ⚡ Rate limiting
* 🌐 React frontend (Chat UI)

---

## 🏆 Why This Project?

This project demonstrates:

* Real-world backend design
* LLM integration without external APIs
* Scalable architecture for AI systems
* Clean separation of concerns

---

## 👨‍💻 Author

Built by Imti

---

## ⭐ Contribute / Feedback

Feel free to fork, contribute, or open issues!
