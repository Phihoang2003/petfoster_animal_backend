package com.hoangphi.service.impl.images;

import com.hoangphi.entity.Imgs;
import com.hoangphi.repository.ImagesRepository;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.images.ImagesResponse;
import com.hoangphi.service.admin.images.ImageService;
import com.hoangphi.utils.PortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImagesRepository imagesRepository;
    private final PortUtils portUtils;

    @Override
    public byte[] getImage(String fileName) {
        return new byte[0];
    }

    @Override
    public ApiResponse deleteImgs(String id) {
        return null;
    }

    @Override
    public ApiResponse getImagesByIdProduct(String id) {
        if(id.isEmpty()){
            return ApiResponse.builder()
                    .message("Id is empty")
                    .data(null)
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }
        List<Imgs> images=imagesRepository.getImagesByProductId(id);
        if(images.isEmpty()){
            return ApiResponse.builder()
                    .message("Not found")
                    .data(null)
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }
        List<ImagesResponse> imagesResponses=images.stream().map(item->{
            return ImagesResponse.builder()
                    .id(item.getId())
                    .name(item.getNameImg())
                    .image(portUtils.getUrlImage(item.getNameImg()))
                    .build();

        }).toList();


        return ApiResponse.builder()
                .message("Success")
                .data(imagesResponses)
                .errors(false)
                .status(HttpStatus.OK.value())
                .build();
    }

    @Override
    public ApiResponse addImagesByIdProduct(String id, List<MultipartFile> images) {
        return null;
    }

    @Override
    public ApiResponse deleteImage(String id, Integer idImage) {
        return null;
    }
}
