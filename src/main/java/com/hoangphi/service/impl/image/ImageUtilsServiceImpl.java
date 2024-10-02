package com.hoangphi.service.impl.image;

import com.hoangphi.repository.MediasRepository;
import com.hoangphi.service.image.ImageServiceUtils;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.Upload;
import software.amazon.awssdk.transfer.s3.model.UploadRequest;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ImageUtilsServiceImpl implements ImageServiceUtils {
    private final MediasRepository mediasRepository;
    private final S3Presigner s3Presigner;
    @Value("${aws.s3.bucket}")
    private String bucketName;
    private final S3TransferManager transferManager;
    private final S3AsyncClient s3AsyncClient;
    private final ExecutorService executorService;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public String getImage(String fileName) {
        try {
            if(fileName==null){
                return null;
            }
            String preSignUrl=redisTemplate.opsForValue().get(fileName);
            if(preSignUrl!=null){
                return preSignUrl;
            }
//            String savedETag = redisTemplate.opsForValue().get(fileName + ":etag");
//            String cachedUrl = redisTemplate.opsForValue().get(fileName + ":url");
//            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(fileName)
//                    .build();
//
//            HeadObjectResponse headObjectResponse = s3AsyncClient.headObject(headObjectRequest).join();
//            String currentETag = headObjectResponse.eTag();

//            if (savedETag != null && savedETag.equals(currentETag)) {
//                System.out.println("Image has not been modified.");
//                return cacheUrl
//            }
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName).build();


            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .getObjectRequest(getObjectRequest)
                    .signatureDuration(Duration.ofDays(7))
                    .build();

            String preSignedUrl = s3Presigner.presignGetObject(getObjectPresignRequest).url().toString();
            redisTemplate.opsForValue().set(fileName,preSignedUrl,6, TimeUnit.DAYS);
            return preSignedUrl;

        } catch (S3Exception e) {
            if (e.statusCode() == 304) {
                System.out.println("Image has not been modified.");
                return "Not Modified";
            } else {
                throw new RuntimeException("Failed to get image: " + fileName, e);
            }
        }
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files) {
        List<CompletableFuture<String>> futures = files.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return uploadSingleFile(file);
                    } catch (IOException e) {
                        throw new CompletionException(e);
                    }
                }, executorService))
                .toList();

        try {
            CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            allOf.join();
        } catch (CompletionException ex) {
            ex.printStackTrace();
        }

        return futures.stream()
                .map(future -> {
                    try {
                        return future.join();
                    } catch (CompletionException ex) {
                        ex.printStackTrace();
                        return "Failed";
                    }
                })
                .collect(Collectors.toList());
    }

    private String uploadSingleFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .cacheControl("public, max-age=86400")
                .build();

        try {
            UploadRequest uploadRequest = UploadRequest.builder()
                    .putObjectRequest(putObjectRequest)
                    .requestBody(AsyncRequestBody.fromInputStream(file.getInputStream(), file.getSize(), executorService))
                    .build();

            Upload upload = transferManager.upload(uploadRequest);
            upload.completionFuture().join();

//            String eTag = completedUpload.response().eTag();
//            System.out.println("Uploaded file with ETag: " + eTag);
//
//            redisTemplate.opsForValue().set(fileName + ":etag", eTag);

            return fileName;
        } catch (S3Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to upload file: " + fileName, e);
        }
    }

    @Override
    public void deleteImage(String fileName) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            CompletableFuture<DeleteObjectResponse> deleteFuture = s3AsyncClient.deleteObject(deleteObjectRequest);
            deleteFuture.join();

//            redisTemplate.delete(fileName + ":etag");
            redisTemplate.delete(fileName);
            System.out.println("Deleted image: " + fileName);
        } catch (S3Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete image: " + fileName, e);
        }
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

