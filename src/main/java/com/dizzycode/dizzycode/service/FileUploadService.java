package com.dizzycode.dizzycode.service;

import com.dizzycode.dizzycode.dto.file.FileCreateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

@Service
public class FileUploadService {

    private final Path fileStorageLocation;

    public FileUploadService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("디렉토리를 생성할 수 없습니다.", e);
        }
    }

    public String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    public String storeBinaryData(FileCreateDTO fileCreateDTO) throws IOException {
        String fileName = StringUtils.cleanPath(fileCreateDTO.getFileName());
        byte[] decodedBytes = Base64.getDecoder().decode(fileCreateDTO.getEncodedFile());
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.write(targetLocation, decodedBytes);
        return fileName;
    }
}
