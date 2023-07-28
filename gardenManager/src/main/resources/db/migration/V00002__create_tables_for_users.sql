CREATE TABLE Users (
    user_id VARCHAR(36),
    user_username VARCHAR(100),
    user_password VARCHAR(100),
    user_role VARCHAR(10),

    PRIMARY KEY (user_id)
);

INSERT INTO Users (user_id, user_username, user_password, user_role) VALUES
    ('23193a1b-5691-40db-9346-1e6c76aa2d45', 'device1', '$2a$12$ZLDa629yvqOV02c6hOTQtOioPt7MCUIhO63lWYuVSGXZo3WSsDViO', 'DEVICE'), -- password: devicePassword
    ('d1c76a21-ed8c-46a0-a059-fd86064a05be', 'admin', '$2a$12$qhtwOW2yt9W0WgUXwmq53.r2JLeY2rqHoWSzeuf7nv0Tf/xhDYeay', 'ADMIN'); -- password: admin