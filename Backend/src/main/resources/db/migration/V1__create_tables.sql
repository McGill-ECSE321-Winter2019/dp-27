--VERSION 1 DDL CO-OPERATOR
CREATE TABLE author (
    id INTEGER PRIMARY KEY,
    email VARCHAR(255) UNIQUE,
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);

CREATE TABLE admin (
    id INTEGER PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES author(id) ON DELETE CASCADE
);

CREATE TABLE company (
    id INTEGER PRIMARY KEY,
    city VARCHAR(255),
    country VARCHAR(255),
    name VARCHAR(255),
    region VARCHAR(255)
);

CREATE TABLE course (
    id INTEGER PRIMARY KEY,
    name varchar(255) UNIQUE
);

CREATE TABLE course_offering (
    id INTEGER PRIMARY KEY,
    season INTEGER,
    year INTEGER NOT NULL,
    course_id INTEGER NOT NULL,
    FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
);

CREATE TABLE student (
    id INTEGER PRIMARY KEY,
    student_id VARCHAR(255) UNIQUE,
    FOREIGN KEY (id) REFERENCES author(id) ON DELETE CASCADE
);

CREATE TABLE coop (
    id INTEGER PRIMARY KEY,
    status INTEGER,
    course_offering_id INTEGER NOT NULL,
    student_id INTEGER NOT NULL,
    FOREIGN KEY (course_offering_id) REFERENCES course_offering(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE
);

CREATE TABLE employer_contact (
    id INTEGER PRIMARY KEY,
    phone_number VARCHAR(255),
    company_id INTEGER NOT NULL,
    FOREIGN KEY (company_id) REFERENCES company(id) ON DELETE CASCADE,
    FOREIGN KEY (id) REFERENCES author(id) ON DELETE CASCADE
);

CREATE TABLE coop_details (
    id INTEGER PRIMARY KEY,
    hours_per_week INTEGER NOT NULL,
    pay_per_hour INTEGER NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    coop_id INTEGER,
    employer_contact_id INTEGER NOT NULL,
    FOREIGN KEY (coop_id) REFERENCES coop(id) ON DELETE CASCADE,
    FOREIGN KEY (employer_contact_id) REFERENCES employer_contact(id)
);

CREATE TABLE report (
    id INTEGER PRIMARY KEY,
    data BYTEA,
    status INTEGER,
    title VARCHAR(255),
    type VARCHAR(255),
    coop_id INTEGER,
    author_id INTEGER,
    FOREIGN KEY (coop_id) REFERENCES coop(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES author(id)
);

CREATE TABLE report_config (
    id INTEGER PRIMARY KEY,
    deadline INTEGER NOT NULL,
    is_deadline_from_start BOOLEAN NOT NULL,
    requires_file BOOLEAN NOT NULL,
    type VARCHAR(255)
);

CREATE TABLE report_section_config (
    id INTEGER PRIMARY KEY,
    question_number INTEGER NOT NULL,
    response_type INTEGER,
    section_prompt VARCHAR(255),
    report_config_id INTEGER NOT NULL,
    FOREIGN KEY (report_config_id) REFERENCES report_config(id) ON DELETE CASCADE
);

CREATE TABLE report_section (
    id INTEGER PRIMARY KEY,
    response TEXT,
    report_id INTEGER NOT NULL,
    report_section_config_id INTEGER NOT NULL,
    FOREIGN KEY (report_id) REFERENCES report(id) ON DELETE CASCADE,
    FOREIGN KEY (report_section_config_id) REFERENCES report_section_config(id) ON DELETE CASCADE
);

CREATE TABLE notification (
    id INTEGER PRIMARY KEY,
    body VARCHAR(255),
    seen BOOLEAN,
    time_stamp BIGINT NOT NULL,
    title VARCHAR(255),
    sender_id INTEGER NOT NULL,
    student_id INTEGER NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES admin(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE
);


CREATE SEQUENCE public.hibernate_sequence;