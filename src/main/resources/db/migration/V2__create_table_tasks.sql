CREATE TABLE tasks (
    id UUID PRIMARY KEY,
    name varchar(255) NOT NULL,
    description varchar(255),
    tasks_status varchar(20) DEFAULT 'ACTIVE',
    tasks_points integer NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
)