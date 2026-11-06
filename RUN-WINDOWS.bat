@echo off
title WarrantyWallet
echo ==========================================
echo       WarrantyWallet - Local App
echo ==========================================
where mvn >nul 2>nul
if errorlevel 1 (
 echo Maven was not found on PATH.
 echo Use the VS Code terminal or install Maven and reopen it.
 pause
 exit /b 1
)
call mvn org.springframework.boot:spring-boot-maven-plugin:3.5.7:run
pause
