package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.record.FacultyRecord;
import ru.hogwarts.school.record.StudentRecord;
import ru.hogwarts.school.service.StudentService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;


@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public StudentRecord create(@RequestBody @Valid StudentRecord studentRecord) {
        return studentService.create(studentRecord);
    }

    @GetMapping("/{id}")
    public StudentRecord read(@PathVariable long id) {
        return studentService.read(id);
    }

    @PutMapping("/{id}")
    public StudentRecord update(@PathVariable long id, @RequestBody @Valid StudentRecord studentRecord) {
        return studentService.update(id, studentRecord);
    }

    @DeleteMapping("/{id}")
    public StudentRecord delete(@PathVariable long id) {
        return studentService.delete(id);
    }

    @GetMapping
    public Collection<StudentRecord> findByAge(@RequestParam int age) {
        return studentService.findByAge(age);
    }

    @GetMapping(params = {"min", "max"})
    public Collection<StudentRecord> findByAgeBetween(@RequestParam("min") int minAge,
                                                      @RequestParam("max") int maxAge) {
        return studentService.findByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/{id}/faculty")
    public FacultyRecord findFacultyByStudent(@PathVariable long id) {
        return studentService.findFacultyByStudent(id);
    }

    @GetMapping("/totalCount")
    public int totalCountOfStudents() {
        return studentService.totalCountOfStudents();
    }

    @GetMapping("/averageAge")
    public double averageAgeOfStudents() {
        return studentService.averageAgeOfStudents();
    }

    @GetMapping("/lastFiveStudents")
    public List<StudentRecord> lastFiveStudents(@RequestParam @Min(1) @Max(10) int count){
        return studentService.lastFiveStudents(count);
    }

    @GetMapping("/findStudentNamesWhichStartedWithA")
    public Stream<String> findStudentNamesWhichStartedWithA(){
        return studentService.findStudentNamesWhichStartedWithA();
    }

    @GetMapping("/findStudentAverageAge")
    public double findStudentAverageAge(){
        return studentService.findStudentAverageAge();
    }
}
