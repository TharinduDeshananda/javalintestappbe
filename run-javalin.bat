@echo off
:start
echo Running Javalin app...
mvn exec:java -Dexec.mainClass="com.tdedsh.App" -Dexec.args="arg1 arg2"

REM Check if the app exited with an error
if %errorlevel% neq 0 (
    echo Application exited with an error. Press 'r' to restart or 'p' to quit...
) else (
    echo Application exited normally. Press 'r' to restart or 'p' to quit...
)

:key_listener
echo Press 'r' to restart or 'p' to quit...
choice /c rp /n /t 1 /d r >nul

if errorlevel 2 goto quit
if errorlevel 1 goto restart

:restart
echo Restarting Javalin app...
goto start

:quit
echo Quitting Javalin app...
exit