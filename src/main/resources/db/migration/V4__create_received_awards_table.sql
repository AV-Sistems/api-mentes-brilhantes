-- Create received_awards table
CREATE TABLE received_awards (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- Create junction table for many-to-many relationship between users and received_awards
CREATE TABLE user_received_awards (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    received_award_id UUID NOT NULL,
    awarded_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,

    CONSTRAINT fk_user_received_awards_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_received_awards_award FOREIGN KEY (received_award_id) REFERENCES received_awards(id),
    CONSTRAINT uk_user_received_awards UNIQUE(user_id, received_award_id)
);

