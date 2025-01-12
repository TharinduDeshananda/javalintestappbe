#!/bin/bash

# Main class and arguments
MAIN_CLASS="com.tdedsh.App"
ARGS="arg1 arg2"

# Function to run the Maven command
run_app() {
    echo "Starting the application..."
    # Start Maven in the background and save its PID
    mvn exec:java -Dexec.mainClass="$MAIN_CLASS" -Dexec.args="$ARGS" &
    APP_PID=$!
}

# Function to terminate the application
terminate_app() {
    if ps -p $APP_PID > /dev/null 2>&1; then
        echo "Stopping the application..."
        # Kill the process and any child processes using taskkill
        taskkill //PID $APP_PID //T //F > /dev/null 2>&1
        wait $APP_PID 2>/dev/null
    fi
}

# Start the app for the first time
run_app

# Interactive loop
while true; do
    echo "Press 'q' to quit, 'r' to restart."
    read -r -n1 key
    echo "" # Newline after keypress

    case $key in
        q)
            terminate_app
            echo "Exiting..."
            exit 0
            ;;
        r)
            terminate_app
            run_app
            ;;
        *)
            echo "Invalid option. Press 'q' to quit or 'r' to restart."
            ;;
    esac
done
