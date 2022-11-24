package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.component.RecordMapper;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.record.AvatarRecord;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;
    private final RecordMapper recordMapper;
    private static final Logger LOG = LoggerFactory.getLogger(AvatarService.class);

    @Value("${application.avatar.store}")
    private String avatarDir;
    private String folderForAvatars;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository, RecordMapper recordMapper) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
        this.recordMapper = recordMapper;
    }

//    public void uploadAvatar(MultipartFile multipartFile) throws IOException {
//        LOG.debug("Method uploadAvatar was invoked");
//        byte[] data = multipartFile.getBytes();
//        Avatar avatar = create(multipartFile.getSize(), multipartFile.getContentType(), data);
//
//        String extension = Optional.ofNullable(multipartFile.getOriginalFilename())
//                .map(s -> s.substring(multipartFile.getOriginalFilename().lastIndexOf('.')))
//                .orElse("");
//        Path path = Paths.get(folderForAvatars).resolve(avatar.getId() + extension);
//        Files.write(path, data);
//        avatar.setFilePath(path.toString());
//        avatarRepository.save(avatar);
//    }


    public AvatarRecord create(MultipartFile multipartFile, long studentId) throws IOException {
        LOG.debug("Method create was invoked");
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new StudentNotFoundException(studentId));
        byte[] data = multipartFile.getBytes();

        String extension = Optional.ofNullable(multipartFile.getOriginalFilename())
                .map(fileName -> fileName.substring(multipartFile.getOriginalFilename().lastIndexOf('.')))
                .orElse("");
        Path path = Paths.get(avatarDir).resolve(studentId + extension);
        Files.write(path, data);

        Avatar avatar = new Avatar();
        avatar.setData(data);
        avatar.setFileSize(data.length);
        avatar.setMediaType(multipartFile.getContentType());
        avatar.setStudent(student);
        avatar.setFilePath(path.toString());

        return recordMapper.toRecord(avatarRepository.save(avatar));
    }

    public Pair<byte[], String> readFromFs(long id) throws IOException {
        LOG.debug("Method readFromFs was invoked");
        Avatar avatar = avatarRepository.findById(id).orElseThrow(() -> new AvatarNotFoundException(id));
        return Pair.of(Files.readAllBytes(Paths.get(avatar.getFilePath())), avatar.getMediaType());
    }

    public Pair<byte[], String> readFromDb(long id) {
        LOG.debug("Method readFromDb was invoked");
        Avatar avatar = avatarRepository.findById(id).orElseThrow(() -> new AvatarNotFoundException(id));
        return Pair.of(avatar.getData(), avatar.getMediaType());
    }

    public Collection<AvatarRecord> getAllByPage(int pageNumber, int pageSize) {
        LOG.debug("Method getAllByPage was invoked");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent().stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }
}
