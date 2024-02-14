package com.sparta.newsfeed.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 기능 : 아마존 s3 테스트용 컨트롤러
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AmazonS3Controller {

    private final AwsS3Uploader s3Uploader;

//    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public String uploadImage(@RequestPart List<MultipartFile> imageList,
//                              @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
//        s3Uploader.upload(imageList, "static", userDetails.getUser());    // static은 버킷 하위에 생성할 폴더 이름
//        return "success";
//    }

//    @DeleteMapping("/resource")
//    public void remove(AwsS3 awsS3) {
//        s3Uploader.deleteFile();
//    }
}
