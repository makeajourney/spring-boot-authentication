CREATE TABLE MEMBER (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255),
    password VARCHAR(255),
    UNIQUE (email)
);

INSERT INTO MEMBER (email, password) VALUES
    ('rose@carrotins.com', '12carrot!@'),
    ('maeve@carrotins.com', '12carrot!@');
