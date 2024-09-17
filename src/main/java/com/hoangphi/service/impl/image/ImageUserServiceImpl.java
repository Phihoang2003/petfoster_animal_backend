package com.hoangphi.service.impl.image;

import com.hoangphi.entity.social.Medias;
import com.hoangphi.repository.MediasRepository;
import com.hoangphi.service.image.ImageServiceUtils;
import com.hoangphi.service.impl.images.item.GetMediasItem;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.Upload;
import software.amazon.awssdk.transfer.s3.model.UploadRequest;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ImageUserServiceImpl implements ImageServiceUtils {
    private final MediasRepository mediasRepository;
    private final S3Presigner s3Presigner;
    @Value("${aws.s3.bucket}")
    private String bucketName;
    private final S3TransferManager transferManager;
    private final ExecutorService executorService;


    @Override
    public String getImage(String fileName) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(Duration.ofDays(30))
                .build();

        return s3Presigner.presignGetObject(getObjectPresignRequest)
                .url().toString();
    }

//    @Override
//    public GetMediasItem getMedias(String fileName, String pathName) {
//        Medias media = mediasRepository.findByName(fileName);
//
//        GetMediasItem getMediasItem = GetMediasItem.builder()
//                .data(this.getImage(fileName, pathName, "planhorder-image.png")).build();
//
//        if (media != null) {
//            File file = new File("images/medias/" + media.getName());
//            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
//
//            getMediasItem.setContentType(mimeType);
//            getMediasItem.setOriginaFile(file);
//        }
//
//        return getMediasItem;
//    }

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

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.join();

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
                .build();

        try {
            UploadRequest uploadRequest = UploadRequest.builder()
                    .putObjectRequest(putObjectRequest)
                    .requestBody(AsyncRequestBody.fromInputStream(file.getInputStream(), file.getSize(), executorService))
                    .build();

            Upload upload = transferManager.upload(uploadRequest);
            upload.completionFuture().join();

            return fileName;
        } catch (S3Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to upload file: " + fileName, e);
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
