CREATE DATABASE ai_code_helper DEFAULT CHARACTER SET utf8mb4;

USE ai_code_helper;

CREATE TABLE chat_memory (
     memory_id VARCHAR(64) PRIMARY KEY,
     messages   LONGTEXT NOT NULL
);