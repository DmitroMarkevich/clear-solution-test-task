CREATE TABLE users
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP,
    first_name   VARCHAR(50)             NOT NULL,
    last_name    VARCHAR(50)             NOT NULL,
    birth_date   DATE                    NOT NULL,
    phone_number VARCHAR(20),
    address      VARCHAR(255),
    email        VARCHAR(50) UNIQUE      NOT NULL CHECK (LENGTH(email) >= 5),
    password     VARCHAR(100)            NOT NULL CHECK (LENGTH(password) >= 8),
    is_deleted   BOOLEAN   DEFAULT FALSE NOT NULL
);

CREATE TABLE roles
(
    id   bigint      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(50) NOT NULL
);

INSERT INTO roles (name)
VALUES ('ROLE_USER'),
       ('ROLE_ADMIN');

CREATE TABLE users_roles
(
    user_id UUID NOT NULL,
    role_id int  NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

INSERT INTO users (id, email, password, first_name, last_name, birth_date)
VALUES ('550e8400-e29b-41d4-a716-446653440000', 'admin@email.com',
        '$2a$10$8xJ8mUVFy4f3Brklo9zZUObw4MjHdKn5aJGh0gOYh0dtQ0/qZDdyW', 'admin', 'Admin',
        '2000-01-01'); -- password: test123qqq!


INSERT INTO users_roles(user_id, role_id)
VALUES ('550e8400-e29b-41d4-a716-446653440000', 2);