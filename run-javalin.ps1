# Function to compile the project
function Compile-Project {
    Write-Host "Compiling the project..."
    mvn compile
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Compilation failed. Exiting..."
        exit 1
    }
}

# Function to start the Java app
function Start-JavaApp {
    Write-Host "Running Javalin app..."
    # Start the Maven process and capture its process ID
    $global:MavenProcess = Start-Process -NoNewWindow -FilePath "mvn" -ArgumentList "exec:java -Dexec.mainClass=`"com.tdedsh.App`" -Dexec.args=`"arg1 arg2`"" -PassThru
}

# Function to stop the Java app
function Stop-JavaApp {
    # Stop the Maven process
    if ($global:MavenProcess -and -not $global:MavenProcess.HasExited) {
        Write-Host "Stopping Maven process..."
        Stop-Process -Id $global:MavenProcess.Id -Force
    }

    # Stop any lingering Java processes spawned by Maven
    $javaProcesses = Get-Process -Name "java" -ErrorAction SilentlyContinue
    foreach ($process in $javaProcesses) {
        Write-Host "Stopping Java process (PID: $($process.Id))..."
        Stop-Process -Id $process.Id -Force
    }
}

# Main script logic
Write-Host "Press 'r' to restart or 'p' to quit..."

# Compile the project before running
Compile-Project

# Start the Java app
Start-JavaApp

while ($true) {
    if ($Host.UI.RawUI.KeyAvailable) {
        $key = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown").Character
        if ($key -eq 'r') {
            Stop-JavaApp
            Write-Host "Restarting Javalin app..."
            Compile-Project
            Start-JavaApp
        } elseif ($key -eq 'p') {
            Stop-JavaApp
            Write-Host "Quitting Javalin app..."
            break
        }
    }
    Start-Sleep -Milliseconds 100
}