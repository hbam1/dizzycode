package com.dizzycode.dizzycode.controller.FileUpload;

import com.dizzycode.dizzycode.dto.file.FileCreateDTO;
import com.dizzycode.dizzycode.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        try {
            String fileName = fileUploadService.storeFile(file);
            String fileUrl = "/uploads/" + fileName;
            return ResponseEntity.ok().body(Map.of("url", fileUrl));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/upload/binary")
    public ResponseEntity<?> uploadBinaryData(@RequestBody FileCreateDTO fileCreateDTO) {
        try {
            String fileName = fileUploadService.storeBinaryData(fileCreateDTO);
            String fileUrl = "/uploads/" + fileName;
            return ResponseEntity.ok().body(Map.of("url", fileUrl));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }
}
