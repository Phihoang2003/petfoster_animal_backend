package com.hoangphi.service.impl.images;

import com.hoangphi.entity.Imgs;
import com.hoangphi.entity.Product;
import com.hoangphi.repository.ImagesRepository;
import com.hoangphi.repository.ProductRepository;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.images.ImagesResponse;
import com.hoangphi.service.admin.images.ImageService;
import com.hoangphi.utils.ImageUtils;
import com.hoangphi.utils.PortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImagesRepository imagesRepository;
    private final PortUtils portUtils;
    private final ProductRepository productRepository;

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
        if (id.isEmpty()) {
            return ApiResponse.builder()
                    .message("Id is empty")
                    .data(null)
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }
        List<Imgs> images = imagesRepository.getImagesByProductId(id);
        if (images.isEmpty()) {
            return ApiResponse.builder()
                    .message("Not found")
                    .data(null)
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }
        List<ImagesResponse> imagesResponses = images.stream().map(item -> {
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
        final int MAX_IMAGE = 4;
        if (id.isEmpty() || images.isEmpty()) {
            return ApiResponse.builder()
                    .message("Id or images is empty")
                    .data(null)
                    .errors(true)
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }

        if (Objects.equals(images.get(0).getOriginalFilename(), "")) {
            return ApiResponse.builder()
                    .message("Images or Id invalid ")
                    .status(400)
                    .errors(true)
                    .data(null)
                    .build();
        }
        Product product=productRepository.findById(id).orElse(null);
        List<Imgs> imageOfProduct=imagesRepository.getImagesByProductId(id);
        if (product==null){
            return ApiResponse.builder()
                    .message("Product not found")
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(true)
                    .data(null)
                    .build();
        }
        if(images.size()+imageOfProduct.size()>MAX_IMAGE){
            return ApiResponse.builder()
                    .message("Limit image for product is 4")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .errors(true)
                    .data(null)
                    .build();
        }
        List<Imgs> newListImgs=images.stream().map(image->{
            try{
                File file= ImageUtils.createFileImage();
                image.transferTo(new File(file.getAbsolutePath()));
                return Imgs.builder()
                        .nameImg(file.getName())
                        .product(product)
                        .build();

            }catch(Exception e){
                System.out.println(e.getMessage());
                return null;
            }
        }).toList();

        return ApiResponse.builder()
                .message("Add images successfuly")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(imagesRepository.saveAll(newListImgs))
                .build();
    }

    @Override
    public ApiResponse deleteImage(String id, Integer idImage) {
        return null;
    }
}
