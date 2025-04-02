package com.ll.nbe344team7.global.imageFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ImageServiceTest {

    @Mock
    private AmazonS3 amazonS3;

    @Mock
    private ImageFileRepository imageFileRepository;

    @InjectMocks
    private S3ImageService s3ImageService;

    private final String bucketName = "test-bucket";

    @BeforeEach
    void setUp() throws Exception {
        // Reflection을 사용하여 private 필드(bucketName) 설정
        var bucketField = S3ImageService.class.getDeclaredField("bucketName");
        bucketField.setAccessible(true);
        bucketField.set(s3ImageService, bucketName);
    }

    @Test
    @DisplayName("파일 업로드 성공 시 S3 URL 반환")
    void t1() throws Exception {
        // given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes(StandardCharsets.UTF_8));
        when(file.getInputStream()).thenReturn(inputStream);
        when(file.isEmpty()).thenReturn(false);

        String expectedUrl = "https://" + bucketName + ".s3.amazonaws.com/test.jpg";
        when(amazonS3.getUrl(anyString(), anyString())).thenReturn(new URL(expectedUrl));

        // when
        String uploadedUrl = s3ImageService.upload(file);

        // then
        assertEquals(expectedUrl, uploadedUrl);
        verify(amazonS3, times(1)).putObject(any(PutObjectRequest.class));
    }

    @Test
    @DisplayName("파일 업로드 중 IOException 발생 시 예외 발생")
    void t2() {
        // given
        MultipartFile emptyFile = mock(MultipartFile.class);
        when(emptyFile.isEmpty()).thenReturn(true);

        // when & then
        S3Exception exception = assertThrows(S3Exception.class, () -> s3ImageService.upload(emptyFile));
        assertEquals("EMPTY_FILE_EXCEPTION", exception.getErrorCode().name());
    }

    @Test
    @DisplayName("지원되지 않는 확장자 업로드 시 예외 발생")
    void t3() {
        // given
        MultipartFile invalidFile = mock(MultipartFile.class);
        when(invalidFile.getOriginalFilename()).thenReturn("invalid.txt");
        when(invalidFile.isEmpty()).thenReturn(false);

        // when & then
        S3Exception exception = assertThrows(S3Exception.class, () -> s3ImageService.upload(invalidFile));
        assertEquals("INVALID_FILE_EXTENTION", exception.getErrorCode().name());
    }

    @Test
    @DisplayName("S3에서 이미지 삭제")
    void t4() {
        // given
        String imageUrl = "https://" + bucketName + ".s3.amazonaws.com/test.jpg";

        // when
        s3ImageService.deleteImageFromS3(imageUrl);

        // then
        verify(amazonS3, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }
}
