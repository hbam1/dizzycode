package com.dizzycode.dizzycode.common.FileUpload;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

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
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = StringUtils.getFilenameExtension(originalFileName);
        String fileName = UUID.randomUUID().toString() + (fileExtension != null ? "." + fileExtension : "");
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    public List<String> storeFiles(List<MultipartFile> files) throws IOException {
        List<String> fileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = storeFile(file);
            fileNames.add(fileName);
        }
        return fileNames;
    }

    public String storeBinaryData(FileCreateDTO fileCreateDTO) throws IOException {
        String originalFileName = StringUtils.cleanPath(fileCreateDTO.getFileName());
        String fileExtension = StringUtils.getFilenameExtension(originalFileName);
        String fileName = UUID.randomUUID().toString() + (fileExtension != null ? "." + fileExtension : "");
        byte[] decodedBytes = Base64.getDecoder().decode(fileCreateDTO.getEncodedFile());
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.write(targetLocation, decodedBytes);
        return fileName;
    }

    public List<String> storeBinaryDatas(List<FileCreateDTO> fileCreateDTOs) throws IOException {
        List<String> fileNames = new ArrayList<>();
        for (FileCreateDTO fileCreateDTO : fileCreateDTOs) {
            String fileName = storeBinaryData(fileCreateDTO);
            fileNames.add(fileName);
        }
        return fileNames;
    }
}
