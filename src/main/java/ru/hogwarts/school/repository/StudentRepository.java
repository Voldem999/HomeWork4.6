package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.entity.Student;

import java.util.Collection;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findAllByAge(int age);

    Collection<Student> findAllByAgeBetween(int minAge, int maxAge);

    @Query(value = "select count(*) from students", nativeQuery = true)
    int totalCountOfStudents();

    @Query(value = "select avg(age) from students", nativeQuery = true)
    double averageAgeOfStudents();

    @Query(value = "select * from students order by id desc limit 5", nativeQuery = true)
    List<Student> lastFiveStudents(@Param("count") int count);
}
