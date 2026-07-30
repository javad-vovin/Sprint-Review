# Sprint Code Review Tracker

A modern, offline-first Android application designed to track and distribute engineering team code reviews (R1 & R2) across sprints with analytics and customizable team management.

Built with **Jetpack Compose**, **Kotlin**, and **Room Database**.

---

## ✨ Features

- **📊 Code Review Distribution (R1 & R2):** Effortlessly increment and decrement First Round (R1) and Second Round (R2) code review counts for every team member.
- **🏃 Sprint Management:** Create new sprints, select active or historical sprints, and configure sprint preferences (start day and duration in weeks).
- **👥 Team Member Management:** Add, edit, or remove engineering team members with custom avatar choices and initial generators.
- **📈 Analytics & Statistics:** View real-time review totals, average reviews per member, top reviewer highlights, and cross-sprint comparisons.
- **🌙 Flexible Theme & Dark Mode:** Choose between System Auto Schedule, Light Theme, or Dark Theme with instant persistence.
- **📱 Responsive & Edge-to-Edge:** Full system insets support with floating bottom bar navigation and responsive Material 3 design.

---

## 🛠️ Tech Stack & Architecture

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material Design 3)
- **Architecture:** MVVM (Model-View-ViewModel) + Clean Data Architecture
- **Database:** Room Database (KSP) for offline persistence
- **State Management:** Kotlin Coroutines & `StateFlow` / `collectAsStateWithLifecycle`
- **Navigation:** Type-safe custom floating bottom bar navigation

---

## 📂 Project Structure

```text
app/src/main/java/ir/siva/sprintreview/
├── data/
│   ├── dao/             # Room Data Access Objects (SprintDao, ReviewRecordDao)
│   ├── database/        # AppDatabase Room initialization
│   ├── model/           # Room entities (Sprint, ReviewRecord)
│   └── repository/      # CodeReviewRepository
├── ui/
│   ├── components/      # Reusable UI components (Avatars, Cards, Dialogs)
│   ├── screens/         # Compose screens (MainDashboard, Statistics, Team, Settings)
│   ├── theme/           # Color schemes, Typography, and ThemeMode
│   └── viewmodel/       # CodeReviewViewModel & UiStates
└── MainActivity.kt      # Main entry point & theme state container
```

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Ladybug (or newer)
- JDK 17
- Android SDK API Level 36 (Min SDK 24)

### Building the Project

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/sprint-code-review-tracker.git
   cd sprint-code-review-tracker
   ```

2. **Open in Android Studio:**
   - Open Android Studio and choose **Open an Existing Project**.
   - Select the project root folder.

3. **Build & Run:**
   - Sync Gradle and press **Run** (`Shift + F10` / `Control + R`) on an Android emulator or connected device.

---

## 📄 Package Namespace

`ir.siva.sprintreview`
