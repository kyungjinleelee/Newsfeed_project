package com.sparta.newsfeed.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sparta.newsfeed.dto.AwsS3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j(topic = "S3 업로드 로직 Service")
@RequiredArgsConstructor
public class S3Upload {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // MultipartFile을 전달받아 File로 변환한 후, S3에 업로드
    public AwsS3 upload(MultipartFile multipartFile, String dirName) throws IOException {
        File file = convertMultipartFiletoFile(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        return upload(file, dirName);
    }

    // 파일명에 UUID를 붙여서 S3에 업로드 후, 업로드 된 이미지의 key 값과 path를 반환
    private AwsS3 upload(File file, String dirName) {
        String key = randomFileName(file, dirName);     // 객체 이름
        String path = putS3(file, key);                 // 해당 객체의 절대 경로 값

        removeFile(file);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환하며 로컬에 파일 생성됨)

        return AwsS3
                .builder()
                .key(key)
                .path(path)
                .build();      // 업로드 된 파일의 S3 URL 주소 반환
    }

    private String randomFileName(File file, String dirName) {
        return dirName + "/" + UUID.randomUUID() + file.getName();
    }

    // S3 버킷에 이미지 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)  // PublicRead 권한으로 업로드 됨
        );
        return getS3(bucket, fileName);
    }

    private String getS3(String bucket, String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    // 로컬에 있는 이미지 삭제
    private void removeFile(File file) {
        if (file.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 않았습니다.");
        }
    }

    // 변환
    private Optional<File> convertMultipartFiletoFile(MultipartFile multipartFile) throws IOException {
        File file = new File(System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());   // 파일 변환

        if (file.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(file);
        }
        return Optional.empty();
    }

    public  void remove(AwsS3 awsS3) {
        if (!amazonS3.doesObjectExist(bucket, awsS3.getKey())) {
            throw new AmazonS3Exception("Object " + awsS3.getKey() + " does not exist!");
        }
        amazonS3.deleteObject(bucket, awsS3.getKey());
    }
}
