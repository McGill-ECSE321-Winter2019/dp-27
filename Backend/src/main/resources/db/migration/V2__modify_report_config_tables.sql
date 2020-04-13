--V2: 
--A CourseOffering can have many Report Configs
--A Report Config can belong to many Course Offerings
--A Report has one Report Config
--A Report Config can belong to many Reports

ALTER TABLE report
ADD report_config_id INTEGER;

ALTER TABLE report
ADD CONSTRAINT report_report_config_id_fkey 
    FOREIGN KEY (report_config_id) REFERENCES report_config(id);

CREATE TABLE course_offering_report_config(
    course_offering_id INTEGER,
    report_config_id INTEGER,
    CONSTRAINT course_offering_report_config_id_pkey 
        PRIMARY KEY (course_offering_id, report_config_id),
    CONSTRAINT course_offering_report_config_course_offering_id_fkey 
        FOREIGN KEY (course_offering_id) REFERENCES course_offering(id),
    CONSTRAINT course_offering_report_config_report_config_id_fkey
        FOREIGN KEY (report_config_id) REFERENCES report_config(id)
);

