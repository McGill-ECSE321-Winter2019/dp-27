
--V3: add ON DELETE CASCADE to course_offering_report_config table

DROP TABLE IF EXISTS course_offering_report_config;

CREATE TABLE course_offering_report_config(
    course_offering_id INTEGER,
    report_config_id INTEGER,
    CONSTRAINT course_offering_report_config_id_pkey 
        PRIMARY KEY (course_offering_id, report_config_id),
    CONSTRAINT course_offering_report_config_course_offering_id_fkey 
        FOREIGN KEY (course_offering_id) REFERENCES course_offering(id)
        ON DELETE CASCADE,
    CONSTRAINT course_offering_report_config_report_config_id_fkey
        FOREIGN KEY (report_config_id) REFERENCES report_config(id)
        ON DELETE CASCADE
);