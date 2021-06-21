CREATE TABLE MEMBER (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255),
    password VARCHAR(255),
    role VARCHAR(255),
    UNIQUE (email)
);

INSERT INTO MEMBER (email, password) VALUES
    ('rose@carrotins.com', '$2a$10$AQittxgTHAP1CyYtLWIbv.gxhiVeq70yniiz9HsWx6m7EHtf.dgd.'),
    ('maeve@carrotins.com', '$2a$10$AQittxgTHAP1CyYtLWIbv.gxhiVeq70yniiz9HsWx6m7EHtf.dgd.');
