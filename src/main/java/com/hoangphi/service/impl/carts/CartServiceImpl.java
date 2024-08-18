package com.hoangphi.service.impl.carts;

import com.hoangphi.config.JwtProvider;
import com.hoangphi.constant.RespMessage;
import com.hoangphi.entity.*;
import com.hoangphi.repository.CartItemRepository;
import com.hoangphi.repository.CartRepository;
import com.hoangphi.repository.ProductRepoRepository;
import com.hoangphi.repository.UserRepository;
import com.hoangphi.request.carts.CartRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.carts.CartResponse;
import com.hoangphi.service.carts.CartService;
import com.hoangphi.utils.PortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final ProductRepoRepository productRepoRepository;
    private final CartItemRepository cartItemRepository;
    private final PortUtils portUtils;

    @Override
    public ApiResponse createCart(String jwt, CartRequest cartRequest) {
        User user=userRepository.findByUsername(jwtProvider.getUsernameFromToken(jwt)).orElse(null);
        if(user==null){
            return ApiResponse.builder()
                    .message("Invalid Token!!!")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .data(null)
                    .errors(true)
                    .build();
        }
        Carts carts=cartRepository.findCart(user.getId()).orElse(null);
        if(carts==null){
            cartRepository.save(Carts.builder()
                    .user(user)
                    .build());
        }
        ProductRepo productRepo=productRepoRepository.findProductRepoByIdAndSize(cartRequest.getProductId(),cartRequest.getSize());
        if(productRepo==null){
            return ApiResponse.builder()
                    .message("Product not found!!!")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .data(null)
                    .errors(true)
                    .build();
        }

        assert carts != null;
        CartItem item=cartItemRepository.findBySizeAndProductId(carts.getCartId(),cartRequest.getProductId(),cartRequest.getSize()).orElse(null);

        if(item!=null){
            int newQuantity=item.getQuantity()+cartRequest.getQuantity();
            if(newQuantity>productRepo.getQuantity()){
                return ApiResponse.builder()
                        .message("Product out of stock!!!")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .data(null)
                        .errors(true)
                        .build();
            }
            item.setQuantity(newQuantity);
            cartItemRepository.save(item);

            return ApiResponse.builder()
                    .message(RespMessage.SUCCESS.getValue())
                    .status(200).errors(false)
                    .data(buildCartResponse(productRepo, item.getQuantity()))
                    .build();
        }
        if(cartRequest.getQuantity()>productRepo.getQuantity()){
            return ApiResponse.builder()
                    .message("Product out of stock!!!")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .data(null)
                    .errors(true)
                    .build();
        }
        CartItem cartItem=CartItem.builder()
                .cart(carts)
                .quantity(cartRequest.getQuantity())
                .productRepo(productRepo)
                .build();
        cartItemRepository.save(cartItem);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .errors(false)
                .message(RespMessage.SUCCESS.getValue())
                .data(buildCartResponse(productRepo, cartItem.getQuantity()))
                .build();

    }
    private CartResponse buildCartResponse(ProductRepo productRepo, int newQuantity) {

        return CartResponse.builder()
                .id(productRepo.getProduct().getId())
                .brand(productRepo.getProduct().getBrand().getBrand())
                .size(productRepo.getSize())
                .image(portUtils.getUrlImage(productRepo.getProduct().getImgs()
                        .stream()
                        .findFirst()
                        .map(Imgs::getNameImg).orElse(null)))
                .name(productRepo.getProduct().getName())
                .price(productRepo.getOutPrice())
                .quantity(newQuantity)
                .repo(productRepo.getQuantity())
                .build();
    }
}
