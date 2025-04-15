package com.ll.nbe344team7.global.imageFIle.service;

import com.ll.nbe344team7.global.imageFIle.exception.S3Exception;
import com.ll.nbe344team7.global.imageFIle.exception.S3ExceptionCode;
import com.ll.nbe344team7.global.imageFIle.repository.ImageFileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class S3ImageService {

    private final S3Client s3Client;
    private final ImageFileRepository imageFileRepository;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    public S3ImageService(S3Client s3Client, ImageFileRepository imageFileRepository) {
        this.s3Client = s3Client;
        this.imageFileRepository = imageFileRepository;
    }

    public String upload(MultipartFile image) {
        if (image.isEmpty() || image.getOriginalFilename() == null) {
            throw new S3Exception(S3ExceptionCode.EMPTY_FILE_EXCEPTION);
        }
        return uploadImage(image);
    }

    private String uploadImage(MultipartFile image) {
        validateImageFileExtension(image.getOriginalFilename());

        try {
            return uploadImageToS3(image);
        } catch (IOException e) {
            throw new S3Exception(S3ExceptionCode.IO_EXCEPTION_ON_IMAGE_UPLOAD);
        }
    }

    private void validateImageFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new S3Exception(S3ExceptionCode.NO_FILE_EXTENTION);
        }

        String extension = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");

        if (!allowedExtensions.contains(extension)) {
            throw new S3Exception(S3ExceptionCode.INVALID_FILE_EXTENTION);
        }
    }

    private String uploadImageToS3(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        String s3FileName = UUID.randomUUID() + "_" + originalFilename;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3FileName)
                .contentType("image/" + extension)
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(image.getBytes()));
        } catch (Exception e) {
            throw new S3Exception(S3ExceptionCode.PUT_OBJECT_EXCEPTION);
        }

        return "https://" + bucketName + ".s3.amazonaws.com/" + s3FileName;
    }

    public void deleteImageFromS3(String imageAddress) {
        String key = getKeyFromImageAddress(imageAddress);

        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            throw new S3Exception(S3ExceptionCode.IO_EXCEPTION_ON_IMAGE_DELETE);
        }
    }

    private String getKeyFromImageAddress(String imageAddress) {
        try {
            String decodedPath = URLDecoder.decode(imageAddress, StandardCharsets.UTF_8.name());
            return decodedPath.substring(decodedPath.indexOf(".amazonaws.com/") + ".amazonaws.com/".length());
        } catch (Exception e) {
            throw new S3Exception(S3ExceptionCode.IO_EXCEPTION_ON_IMAGE_DELETE);
        }
    }
}