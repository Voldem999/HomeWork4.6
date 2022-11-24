select s.name as name, s.age as age, f.name as faculty FROM students s INNER JOIN faculties f on f.id = s.faculty_id;

select s.name as name, s.age as age from students s INNER JOIN avatars a on s.id = a.student_id;