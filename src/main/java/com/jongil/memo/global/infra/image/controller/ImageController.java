package com.jongil.memo.global.infra.image.controller;

import com.jongil.memo.global.infra.image.Image;
import com.jongil.memo.global.infra.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/image/upload")
@Controller
public class ImageController {
    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<String> upload(MultipartFile file) throws IOException {
        Image image = imageService.upload(file);

        return ResponseEntity.ok("/image/upload/" + image.getPath() + "/" +  image.getName());
    }

    @GetMapping("/{year}/{month}/{day}/{name}")
    public ResponseEntity<Resource> serve(
            @PathVariable String year,
            @PathVariable String month,
            @PathVariable String day,
            @PathVariable String name
    ) throws IOException {
        String path = String.format("%s/%s/%s/%s", year, month, day, name);

        Resource resource = imageService.serve(path);

        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\""
                )
                .body(resource);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> ioExceptionHandler(IOException ignored) {
        return ResponseEntity.notFound().build();
    }
}