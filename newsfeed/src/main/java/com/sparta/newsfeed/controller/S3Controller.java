package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.AwsS3;
import com.sparta.newsfeed.service.S3Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class S3Controller {

    private final S3Upload s3Upload;

    @PostMapping("/resource")
    public AwsS3 upload(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        return s3Upload.upload(multipartFile, "upload");   // upload는 버킷 하위에 생성할 폴더 이름
    }

    @DeleteMapping("/resource")
    public void remove(AwsS3 awsS3) {
        s3Upload.remove(awsS3);
    }
}
