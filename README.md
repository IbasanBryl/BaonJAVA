# BaonBrain

BaonBrain is a Java Swing student cashflow app for tracking income, expenses, savings goals, and budget progress. It includes a styled login screen, a dashboard, account management, local storage, and a small built-in backend for app data.

## Features

- Login and registration flow
- Remembered login support
- Dashboard overview for income, expenses, savings, and forecast
- Category budgets and saving goals
- Account management, reset, and logout actions
- Local JSON and SQLite storage
- Local backend server for app data

## Project Layout

- `src/baon/app/Main.java` - application entry point
- `src/baon/ui/` - Swing UI screens, shared theme, and custom components
- `src/baon/data/` - SQLite/JSON storage and remembered login
- `src/baon/backend/BackendServer.java` - local HTTP backend
- `src/baon/model/` - data models
- `lib/` - bundled dependencies and logo asset
- `data/` - local database files

## Requirements

- Java 11 or newer JDK
- The bundled jars in `lib/`

## Run

If you want to run it from the terminal on Windows PowerShell:

```powershell
$cp = "lib\flatlaf-3.7.jar;lib\sqlite-jdbc-3.46.1.3.jar"
$sources = Get-ChildItem src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -cp $cp -d out $sources
java -cp "out;$cp" baon.app.Main
```

If you use an IDE, add the jars in `lib/` to the project classpath and run `baon.app.Main`.

## Data Files

- `data/baon.db` stores the SQLite database
- `data/database.json` stores the JSON app state
- Both files are created or updated automatically when the app runs

## Backend

The app starts a local backend automatically on `http://127.0.0.1:8080`.

Available endpoints:

- `GET /api/health`
- `GET /api/database`
- `PUT /api/database`

## Notes

- The app is designed to run locally.
- Resetting data clears the current user's stored records.
