package com.dizzycode.dizzycode.common.FileUpload;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(@RequestParam("file") List<MultipartFile> files) {
        try {
            List<String> fileNames = fileUploadService.storeFiles(files);
            List<String> fileUrls = fileNames.stream().map(fileName -> "/uploads/" + fileName).collect(Collectors.toList());
            return ResponseEntity.ok().body(Map.of("urls", fileUrls));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/upload/binary")
    public ResponseEntity<?> uploadBinaryData(@RequestBody List<FileCreateDTO> fileCreateDTOs) {
        try {
            List<String> fileNames = fileUploadService.storeBinaryDatas(fileCreateDTOs);
            List<String> fileUrls = fileNames.stream().map(fileName -> "/uploads/" + fileName).collect(Collectors.toList());
            return ResponseEntity.ok().body(Map.of("urls", fileUrls));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }
}
