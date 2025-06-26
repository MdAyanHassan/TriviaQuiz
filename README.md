# 🧠 Trivia Quiz  
*A dynamic quiz app built with Jetpack Compose*  

![Android](https://img.shields.io/badge/Android-Compose-3DDC84?logo=jetpack-compose) ![Kotlin](https://img.shields.io/badge/Kotlin-100%25-7F52FF?logo=kotlin) ![API](https://img.shields.io/badge/API-OpenTriviaDB-FF5733?logo=json)

---

## 🎯 Features  
- **Fully Compose UI**: Modern declarative interface  
- **Quiz Customization**:  
  - 5+ categories (Science, History, etc.)  
  - Adjustable difficulty (Easy/Medium/Hard)  
  - Question type selection (MCQ/True-False)  
- **Real-time Scoring**: Persistent high score leaderboard  
- **API-Powered**: 50,000+ questions from Open Trivia DB  

---

## 🛠 Tech Stack  
- **UI**: Jetpack Compose (Material 3)  
- **Networking**: Retrofit + Kotlin Coroutines  
- **Persistence**: SharedPreferences  
- **DI**: Manual
- **State Management**: ViewModel + Compose State  

---

## 🚀 Setup  
1. **Clone the repo:**  
   ```bash  
   git clone https://github.com/MdAyanHassan/TriviaQuiz.git  
   ```
2. **Build & Run:**
   - **Via Android Studio:** Open the project, sync Gradle, and click "Run"
   - **Via Command Line:**
     ```bash
     ./gradlew build
     ./gradlew installDebug
     ```  
