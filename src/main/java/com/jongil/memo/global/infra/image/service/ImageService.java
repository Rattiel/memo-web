package com.jongil.memo.global.infra.image.service;

import com.jongil.memo.global.infra.image.Image;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    Image upload(MultipartFile file) throws IOException;

    Resource serve(String path) throws IOException;
}
