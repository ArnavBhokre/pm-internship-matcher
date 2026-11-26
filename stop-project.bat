@echo off
title PM Internship Matcher - Stop Servers
color 0C

echo ===================================================
echo     STOPPING PM INTERNSHIP MATCHER SERVERS
echo ===================================================
echo.

echo Stopping processes on ports 8080 and 5500...
powershell -NoProfile -ExecutionPolicy Bypass -Command "Get-NetTCPConnection -LocalPort 8080,5500 -ErrorAction SilentlyContinue | ForEach-Object { Stop-Process -Id $_.OwningProcess -Force -ErrorAction SilentlyContinue; Write-Host ('Stopped process on port ' + $_.LocalPort) }"

echo.
echo Both Frontend and Backend servers have been stopped cleanly!
ping 127.0.0.1 -n 3 >nul
exit
