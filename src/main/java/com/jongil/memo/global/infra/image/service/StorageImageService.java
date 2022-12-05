package com.jongil.memo.global.infra.image.service;

import com.jongil.memo.global.infra.image.Image;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class StorageImageService implements ImageService {
    @Value("${image.upload.folder:image/upload}")
    private String uploadFolderPath;

    private Path uploadFolder;

    @PostConstruct
    public void init() throws IOException {
        uploadFolder = Paths.get(uploadFolderPath);

        makeOrLoadFolder(uploadFolder);
    }

    @Transactional
    @Override
    public Image upload(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일 업로드 실패(이유: 빈파일)");
        }

        String name = file.getOriginalFilename();

        if (name == null) {
            throw new IllegalArgumentException("파일 업로드 실패(이유: 파일 이름이 존재 하지 않음)");
        }

        int formatIndex = name.lastIndexOf(".");

        if (formatIndex < 0) {
            throw new IllegalArgumentException("파일 업로드 실패(이유: 파일 포맷이 존재 하지 않음)");
        }

        String format = name.substring(formatIndex + 1);

        if (!checkFormat(format)) {
            throw new IllegalArgumentException("파일 업로드 실패(이유: 올바르지 않은 파일 포맷)");
        }

        String uuid = UUID.randomUUID().toString();
        String newName = String.format("%s.%s", uuid, format);
        String path = getSaveFolderPath();

        Path todayFolder = this.uploadFolder.resolve(path);

        makeOrLoadFolder(todayFolder);

        Path filePath = todayFolder.resolve(Paths.get(newName));

        file.transferTo(filePath);

        return new Image(path, newName);
    }

    @Override
    public Resource serve(String path) throws IOException {
        Path file = uploadFolder.resolve(path);

        try {
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException(file.getFileName().toString());
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException(file.getFileName().toString());
        }
    }

    private boolean checkFormat(String format) {
        return switch (format.toLowerCase()) {
            case "jpg", "png", "jpeg", "gif" -> true;
            default -> false;
        };
    }

    private String getSaveFolderPath() {
        LocalDateTime now = LocalDateTime.now();

        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();

        return String.format("%d/%d/%d", year, month, day);
    }

    private void makeOrLoadFolder(Path path) throws IOException {
        if (!Files.exists(path)) {
            if (!path.toFile().mkdirs()) {
                log.error("폴더 생성을 실패하였습니다. (경로 : {})", path.toAbsolutePath());
                throw new IOException("폴더 생성을 실패하였습니다.");
            } else {
                log.info("폴더 생성하였습니다. (경로 : {})", path.toAbsolutePath());
            }
        } else {
            if (!Files.isDirectory(path)) {
                log.error("경로가 폴더가 아닙니다. (경로 : {})", path.toAbsolutePath());
                throw new IOException("경로가 폴더가 아닙니다.");
            } else {
                log.info("폴더가 이미 있습니다. (경로 : {})", path.toAbsolutePath());
            }
        }
    }
}
