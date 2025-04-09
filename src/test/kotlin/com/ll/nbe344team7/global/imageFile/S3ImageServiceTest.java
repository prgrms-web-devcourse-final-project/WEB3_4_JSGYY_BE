package com.ll.nbe344team7.global.imageFile;

import com.ll.nbe344team7.global.imageFIle.exception.S3Exception;
import com.ll.nbe344team7.global.imageFIle.repository.ImageFileRepository;
import com.ll.nbe344team7.global.imageFIle.service.S3ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ImageServiceTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private ImageFileRepository imageFileRepository;

    @InjectMocks
    private S3ImageService s3ImageService;

    private final String bucketName = "test-bucket";

    @BeforeEach
    void setUp() throws Exception {
        var field = S3ImageService.class.getDeclaredField("bucketName");
        field.setAccessible(true);
        field.set(s3ImageService, bucketName);
    }

    @Test
    @DisplayName("파일 업로드 성공 시 S3 URL 반환")
    void uploadSuccess() throws Exception {
        // given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(file.getBytes()).thenReturn("test data".getBytes(StandardCharsets.UTF_8));
        when(file.isEmpty()).thenReturn(false);

        // when
        String resultUrl = s3ImageService.upload(file);

        // then
        assertTrue(resultUrl.startsWith("https://" + bucketName + ".s3.amazonaws.com/"));
        assertTrue(resultUrl.endsWith("test.jpg"));

        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    @DisplayName("빈 파일 업로드 시 예외 발생")
    void uploadEmptyFile() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        S3Exception exception = assertThrows(S3Exception.class, () -> s3ImageService.upload(file));
        assertEquals("EMPTY_FILE_EXCEPTION", exception.getErrorCode().name());
    }

    @Test
    @DisplayName("지원하지 않는 확장자 업로드 시 예외 발생")
    void uploadInvalidExtension() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("invalid.txt");
        when(file.isEmpty()).thenReturn(false);

        S3Exception exception = assertThrows(S3Exception.class, () -> s3ImageService.upload(file));
        assertEquals("INVALID_FILE_EXTENTION", exception.getErrorCode().name());
    }

    @Test
    @DisplayName("S3에서 이미지 삭제")
    void deleteImageFromS3() {
        // given
        String key = UUID.randomUUID() + "_test.jpg";
        String url = "https://" + bucketName + ".s3.amazonaws.com/" + key;

        // when
        s3ImageService.deleteImageFromS3(url);

        // then
        verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }
}
