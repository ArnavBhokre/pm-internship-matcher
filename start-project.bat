@echo off
title PM Internship Matcher - Launcher
color 0A

echo ===================================================
echo     STARTING PM INTERNSHIP MATCHER PLATFORM
echo ===================================================
echo.

cd /d "%~dp0"

echo [1/4] Checking and clearing old processes on ports 8080 and 5500...
powershell -NoProfile -ExecutionPolicy Bypass -Command "Get-NetTCPConnection -LocalPort 8080,5500 -ErrorAction SilentlyContinue | ForEach-Object { Stop-Process -Id $_.OwningProcess -Force -ErrorAction SilentlyContinue }" >nul 2>&1
ping 127.0.0.1 -n 2 >nul

echo [2/4] Starting Backend (Spring Boot on port 8080)...
if exist "backend\target\pm-internship-matcher-1.0.0.jar" (
    start "PM Internship Matcher - Backend" cmd /c "java -jar backend\target\pm-internship-matcher-1.0.0.jar"
) else (
    start "PM Internship Matcher - Backend" cmd /c "cd backend && ..\apache-maven-3.9.16\bin\mvn.cmd spring-boot:run"
)

echo [3/4] Starting Frontend (HTTP Server on port 5500)...
start "PM Internship Matcher - Frontend" cmd /c "cd frontend && python -m http.server 5500"

echo [4/4] Waiting for backend to finish booting...
ping 127.0.0.1 -n 12 >nul

echo.
echo ===================================================
echo   SUCCESS! Application is live!
echo   Frontend: http://localhost:5500/index.html
echo   Backend:  http://localhost:8080
echo ===================================================
echo.
echo Opening browser...
start http://localhost:5500/index.html
exit
