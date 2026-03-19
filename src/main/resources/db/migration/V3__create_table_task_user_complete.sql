CREATE TABLE task_user_completed (
    id UUID PRIMARY KEY,
    task_id UUID NOT NULL,
    user_id UUID NOT NULL,
    verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,

    CONSTRAINT fk_task_user_completed_task FOREIGN KEY (task_id) REFERENCES tasks(id),
    CONSTRAINT fk_task_user_completed_user FOREIGN KEY (user_id) REFERENCES users(id)

);