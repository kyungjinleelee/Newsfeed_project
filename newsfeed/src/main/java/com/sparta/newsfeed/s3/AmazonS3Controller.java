package com.sparta.newsfeed.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

// 기능 : 아마존 s3 테스트용 컨트롤러
@RequiredArgsConstructor
@RequestMapping("/api/upload")
@RestController
public class AmazonS3Controller {

    private final AwsS3Uploader s3Uploader;

//    @PostMapping("/profile")
//    public ResponseEntity<String> uploadProfile(@RequestPart("file") MultipartFile file) throws IOException {
//        String url = s3Uploader.uploadProfile(file, "test");
//        return new ResponseEntity<>(url, HttpStatus.OK);
//    }

    @PostMapping("/multimedia")
    public ResponseEntity<List<String>> uploadMultimedia(@RequestPart("files") List<MultipartFile> files) throws IOException {
        List<String> urls = s3Uploader.uploadMultimedia(files, "test_multimedia");
        return new ResponseEntity<>(urls, HttpStatus.OK);
    }


}
