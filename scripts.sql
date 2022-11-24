select *
from students
where age between 10 and 20;

select name
from students;

select *
from students
where name like '%О%';

select *
from students
where age < id;

select *
from students
order by age;

select *
from students
order by age desc;

select count(*)
from students;

select *
from students
order by id desc
limit 5;

select *
from students
order by id
limit 5;

select avg(age)
from students;


