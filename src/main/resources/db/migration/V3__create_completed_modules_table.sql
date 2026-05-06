-- Create completed_modules table
CREATE TABLE completed_modules (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Create junction table for many-to-many relationship between users and completed_modules
CREATE TABLE user_completed_modules (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    completed_module_id UUID NOT NULL,
    completed_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,

    CONSTRAINT fk_user_completed_modules_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_completed_modules_module FOREIGN KEY (completed_module_id) REFERENCES completed_modules(id),
    CONSTRAINT uk_user_completed_modules UNIQUE(user_id, completed_module_id)
);

