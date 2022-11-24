-- liquibase formatted sql

-- changeset voldem999:1
CREATE INDEX student_name_index on students(name);

-- changeset voldem999:2
CREATE INDEX faculty_name_and_color_index on faculties(name, color);