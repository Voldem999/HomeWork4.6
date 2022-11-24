package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.component.RecordMapper;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.record.FacultyRecord;
import ru.hogwarts.school.record.StudentRecord;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StudentService {

    @Autowired
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final RecordMapper recordMapper;
    private static final Logger LOG = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository, RecordMapper recordMapper, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.recordMapper = recordMapper;
        this.facultyRepository = facultyRepository;
    }

    public StudentRecord create(StudentRecord studentRecord) {
        LOG.debug("Method create was invoked");
        Student student = recordMapper.toEntity(studentRecord);
        Faculty faculty = Optional.ofNullable(studentRecord.getFaculty())
                .map(FacultyRecord::getId)
                .flatMap(facultyRepository::findById)
                .orElse(null);
        student.setFaculty(faculty);
        return recordMapper.toRecord(studentRepository.save(student));
    }

    public StudentRecord read(long id) {
        LOG.debug("Method read was invoked");
        return recordMapper.toRecord(studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id)));
    }

    public StudentRecord update(long id, StudentRecord studentRecord) {
        LOG.debug("Method update was invoked");
        Student oldStudent = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        oldStudent.setName(studentRecord.getName());
        oldStudent.setAge(studentRecord.getAge());
        oldStudent.setFaculty(
                Optional.ofNullable(studentRecord.getFaculty())
                        .map(FacultyRecord::getId)
                        .flatMap(facultyRepository::findById)
                        .orElse(null));
        return recordMapper.toRecord(studentRepository.save(oldStudent));
    }

    public StudentRecord delete(long id) {
        LOG.debug("Method delete was invoked");
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.delete(student);
        return recordMapper.toRecord(student);
    }

    public Collection<StudentRecord> findByAge(int age) {
        LOG.debug("Method findByAge was invoked");
        return studentRepository.findAllByAge(age).stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public Collection<StudentRecord> findByAgeBetween(int minAge, int maxAge) {
        LOG.debug("Method findByAgeBetween was invoked");
        return studentRepository.findAllByAgeBetween(minAge, maxAge).stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public FacultyRecord findFacultyByStudent(long id) {
        LOG.debug("Method findFacultyByStudent was invoked");
        return read(id).getFaculty();
    }

    public Integer totalCountOfStudents() {
        LOG.debug("Method totalCountOfStudents was invoked");
        return studentRepository.totalCountOfStudents();
    }

    public Double averageAgeOfStudents() {
        LOG.debug("Method averageAgeOfStudents was invoked");
        return studentRepository.averageAgeOfStudents();
    }

    public List<StudentRecord> lastFiveStudents(int count) {
        LOG.debug("Method lastFiveStudents was invoked");
        return studentRepository.lastFiveStudents(count).stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public Stream<String> findStudentNamesWhichStartedWithA() {
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(s -> s.startsWith("A"))
                .sorted();
    }

    public double findStudentAverageAge() {
        return studentRepository.findAll().stream()
                .mapToDouble(Student::getAge)
                .average()
                .orElse(0);
    }
}
