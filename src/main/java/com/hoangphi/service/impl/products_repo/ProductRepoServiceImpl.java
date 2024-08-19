package com.hoangphi.service.impl.products_repo;

import com.hoangphi.config.JwtProvider;
import com.hoangphi.entity.PriceChange;
import com.hoangphi.entity.Product;
import com.hoangphi.entity.ProductRepo;
import com.hoangphi.entity.User;
import com.hoangphi.repository.PriceChangeRepository;
import com.hoangphi.repository.ProductRepoRepository;
import com.hoangphi.repository.ProductRepository;
import com.hoangphi.repository.UserRepository;
import com.hoangphi.request.variants.CreateRepoRequest;
import com.hoangphi.request.variants.UpdateRepoRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.admin.products_repo.ProductRepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductRepoServiceImpl implements ProductRepoService {
    private final ProductRepoRepository productRepoRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PriceChangeRepository priceChangeRepository;
    private final JwtProvider jwtProvider;

    @Override
    public ApiResponse deleteProductRepo(Integer id) {
        ProductRepo productRepo = productRepoRepository.findById(id).orElse(null);
        if (productRepo == null) {
            return ApiResponse.builder()
                    .message("Product repo not found !")
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(true)
                    .data(null)
                    .build();
        }

        productRepo.setIsActive(false);

        return ApiResponse.builder()
                .message("Delete success!")
                .errors(false)
                .status(HttpStatus.OK.value())
                .data(productRepoRepository.save(productRepo))
                .build();
    }

    @Override
    public ApiResponse getProductRepositories(String idProduct) {
        if(idProduct.isBlank()){
            return ApiResponse.builder()
                    .message("Product repo not found with " + idProduct)
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(true)
                    .data(null)
                    .build();
        }
        List<ProductRepo> lProductRepos = productRepoRepository.findByProductSorting(idProduct);
        if(lProductRepos.isEmpty()){
            return ApiResponse.builder()
                    .message("Product repo not found with " + idProduct)
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(true)
                    .data(null)
                    .build();
        }
        return ApiResponse.builder()
                .message("Successfully")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(lProductRepos)
                .build();
    }

    @Override
    public ApiResponse addAProductRepository(String idProduct, CreateRepoRequest createRepoRequest) {
        if(idProduct.isBlank()){
            return ApiResponse.builder()
                    .message("Product ID not be blank")
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(true)
                    .data(null)
                    .build();
        }
        Product product=productRepository.findById(idProduct).orElse(null);
        if(product==null){
            return ApiResponse.builder()
                    .message("Product not found")
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(true)
                    .data(null)
                    .build();
        }
        ProductRepo productRepo=ProductRepo.builder()
                .inStock(true)
                .product(product)
                .inPrice(createRepoRequest.getInPrice())
                .outPrice(createRepoRequest.getOutPrice())
                .quantity(createRepoRequest.getQuantity())
                .size(createRepoRequest.getSize())
                .isActive(true)
                .build();
        return ApiResponse.builder()
                .message("Successfully")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(productRepoRepository.save(productRepo)).build();
    }

    @Override
    public ApiResponse updateOrCreateRepo(Integer id, UpdateRepoRequest updateRepoRequest, String token) {
        ProductRepo productRepo = productRepoRepository.findById(id).orElse(null);
        if (productRepo == null) {
            return ApiResponse.builder()
                    .message("Product repo not found !!!")
                    .status(HttpStatus.NOT_FOUND.value())
                    .errors(true)
                    .data(null)
                    .build();
        }
        if(!productRepo.getInPrice().equals(updateRepoRequest.getInPrice())||
                !productRepo.getOutPrice().equals(updateRepoRequest.getOutPrice())){
            User user=userRepository.findByUsername(jwtProvider.getUsernameFromToken(token)).orElse(null);
            if(user==null){
                return ApiResponse.builder()
                        .message("User not found !!!")
                        .status(HttpStatus.NOT_FOUND.value())
                        .errors(true)
                        .data(null)
                        .build();
            }
            PriceChange priceChange=PriceChange.builder()
                    .newInPrice(updateRepoRequest.getInPrice())
                    .newOutPrice(updateRepoRequest.getOutPrice())
                    .oldInPrice(productRepo.getInPrice())
                    .oldOutPrice(productRepo.getOutPrice())
                    .productRepo(productRepo)
                    .user(user)
                    .build();
            priceChangeRepository.save(priceChange);

        }
        productRepo.setInPrice(updateRepoRequest.getInPrice());
        productRepo.setOutPrice(updateRepoRequest.getOutPrice());
        productRepo.setQuantity(updateRepoRequest.getQuantity());

        productRepoRepository.save(productRepo);

        return ApiResponse.builder()
                .message("Successfully")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(productRepo)
                .build();
    }
}
