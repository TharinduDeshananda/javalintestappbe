use taskdb;
-- Drop tables if they already exist (optional, use with caution in production)
DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS users;

-- Create the `users` table if it doesn't exist
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,       -- Unique ID for each user
    username VARCHAR(50) NOT NULL UNIQUE,    -- Unique username
    email VARCHAR(100) NOT NULL UNIQUE,      -- Unique email
    password_hash VARCHAR(255) NOT NULL,     -- Hashed password for security
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Timestamp of user creation
);

-- Create the `tasks` table if it doesn't exist
CREATE TABLE IF NOT EXISTS tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,       -- Unique ID for each task
    user_id INT NOT NULL,                    -- ID of the user who owns the task
    title VARCHAR(100) NOT NULL,             -- Title of the task
    description TEXT,                        -- Description of the task
    status ENUM('TODO', 'IN_PROGRESS', 'DONE') DEFAULT 'TODO', -- Task status
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp of task creation
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Timestamp of last update
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE -- Link tasks to users
);