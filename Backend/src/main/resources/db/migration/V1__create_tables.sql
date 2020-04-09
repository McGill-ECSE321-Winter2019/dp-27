--VERSION 1 DDL CO-OPERATOR
CREATE TABLE author (
    id INTEGER CONSTRAINT author_id_pkey PRIMARY KEY,
    email VARCHAR(255) CONSTRAINT author_email_unique UNIQUE,
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);

CREATE TABLE admin (
    id INTEGER CONSTRAINT admin_id_pkey PRIMARY KEY,
    CONSTRAINT admin_id_fkey FOREIGN KEY (id) REFERENCES author(id) ON DELETE CASCADE
);

CREATE TABLE company (
    id INTEGER CONSTRAINT company_id_pkey PRIMARY KEY,
    city VARCHAR(255),
    country VARCHAR(255),
    name VARCHAR(255),
    region VARCHAR(255)
);

CREATE TABLE course (
    id INTEGER CONSTRAINT course_id_pkey PRIMARY KEY,
    name varchar(255) CONSTRAINT course_name_unique UNIQUE
);

CREATE TABLE course_offering (
    id INTEGER CONSTRAINT course_offering_id_pkey PRIMARY KEY,
    season INTEGER,
    year INTEGER,
    course_id INTEGER,
    CONSTRAINT course_offering_course_id_fkey FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
);

CREATE TABLE student (
    id INTEGER CONSTRAINT student_id_pkey PRIMARY KEY,
    student_id VARCHAR(255) CONSTRAINT student_student_id_unique UNIQUE,
    CONSTRAINT student_id_fkey FOREIGN KEY (id) REFERENCES author(id) ON DELETE CASCADE
);

CREATE TABLE coop (
    id INTEGER CONSTRAINT coop_id_pkey PRIMARY KEY,
    status INTEGER,
    course_offering_id INTEGER,
    student_id INTEGER,
    CONSTRAINT coop_course_offering_id_fkey FOREIGN KEY (course_offering_id) REFERENCES course_offering(id) ON DELETE CASCADE,
    CONSTRAINT coop_student_id_fkey FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE
);

CREATE TABLE employer_contact (
    id INTEGER CONSTRAINT employer_contact_id_pkey PRIMARY KEY,
    phone_number VARCHAR(255),
    company_id INTEGER,
    CONSTRAINT employer_contact_company_id_fkey FOREIGN KEY (company_id) REFERENCES company(id) ON DELETE CASCADE,
    CONSTRAINT employer_contact_id_fkey FOREIGN KEY (id) REFERENCES author(id) ON DELETE CASCADE
);

CREATE TABLE coop_details (
    id INTEGER CONSTRAINT coop_details_id_pkey PRIMARY KEY,
    hours_per_week INTEGER,
    pay_per_hour INTEGER,
    start_date DATE,
    end_date DATE,
    coop_id INTEGER,
    employer_contact_id INTEGER,
    CONSTRAINT coop_details_coop_id_fkey FOREIGN KEY (coop_id) REFERENCES coop(id) ON DELETE CASCADE,
    CONSTRAINT coop_detailis_employer_contact_id_fkey FOREIGN KEY (employer_contact_id) REFERENCES employer_contact(id)
);

CREATE TABLE report (
    id INTEGER CONSTRAINT report_id_pkey PRIMARY KEY,
    data BYTEA,
    status INTEGER,
    title VARCHAR(255),
    type VARCHAR(255),
    coop_id INTEGER,
    author_id INTEGER,
    CONSTRAINT report_coop_id_fkey FOREIGN KEY (coop_id) REFERENCES coop(id) ON DELETE CASCADE,
    CONSTRAINT report_author_id_fkey FOREIGN KEY (author_id) REFERENCES author(id)
);

CREATE TABLE report_config (
    id INTEGER CONSTRAINT report_config_id_pkey PRIMARY KEY,
    deadline INTEGER,
    is_deadline_from_start BOOLEAN,
    requires_file BOOLEAN,
    type VARCHAR(255)
);

CREATE TABLE report_section_config (
    id INTEGER CONSTRAINT report_section_config_id_pkey PRIMARY KEY,
    question_number INTEGER,
    response_type INTEGER,
    section_prompt VARCHAR(255),
    report_config_id INTEGER,
    CONSTRAINT report_section_config_report_config_id_fkey FOREIGN KEY (report_config_id) REFERENCES report_config(id) ON DELETE CASCADE
);

CREATE TABLE report_section (
    id INTEGER CONSTRAINT report_section_id_pkey PRIMARY KEY,
    response TEXT,
    report_id INTEGER,
    report_section_config_id INTEGER,
    CONSTRAINT report_section_report_id_fkey FOREIGN KEY (report_id) REFERENCES report(id) ON DELETE CASCADE,
    CONSTRAINT report_section_report_section_config_id_fkey FOREIGN KEY (report_section_config_id) REFERENCES report_section_config(id) ON DELETE CASCADE
);

CREATE TABLE notification (
    id INTEGER CONSTRAINT notification_id_pkey PRIMARY KEY,
    body VARCHAR(255),
    seen BOOLEAN,
    time_stamp BIGINT,
    title VARCHAR(255),
    sender_id INTEGER,
    student_id INTEGER,
    CONSTRAINT notification_sender_id_fkey FOREIGN KEY (sender_id) REFERENCES admin(id) ON DELETE CASCADE,
    CONSTRAINT notification_student_id_fkey FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE
);


CREATE SEQUENCE public.hibernate_sequence;