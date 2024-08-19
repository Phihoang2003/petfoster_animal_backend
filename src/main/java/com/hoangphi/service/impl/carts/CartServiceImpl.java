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

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public ApiResponse updateCarts(String jwt, List<CartRequest> cartRequests) {
        if (cartRequests.isEmpty()) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .errors(true)
                    .data(null)
                    .message("Cart is empty!!!")
                    .build();
        }
        User user = userRepository.findByUsername(jwtProvider.getUsernameFromToken(jwt)).orElse(null);

        // if user null
        if (user == null) {
            return ApiResponse.builder()
                    .message("Invalid Token!!!")
                    .status(400)
                    .errors("Invalid token")
                    .build();
        }

        Carts carts = cartRepository.findCart(user.getId()).orElse(null);

        if (carts == null) {
            return ApiResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errors(true)
                    .data(null)
                    .message(RespMessage.INTERNAL_SERVER_ERROR.getValue())
                    .build();
        }
        List<CartItem> cartItems = cartItemRepository.findByCartsId(carts.getCartId());

        if (cartItems.isEmpty()) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .errors(true)
                    .data(null)
                    .message("Cart is empty!!!")
                    .build();
        }
        cartItemRepository.deleteAll(cartItems);
        List<CartItem> newItems=new ArrayList<>();
        for(CartRequest item:cartRequests){
            ProductRepo productRepo=productRepoRepository.findProductRepoByIdAndSize(item.getProductId(),item.getSize());
            if(productRepo==null){
                return ApiResponse.builder()
                        .message("Product not found!!!")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .data(null)
                        .errors(true)
                        .build();
            }
            if(item.getQuantity()>productRepo.getQuantity()){
                return ApiResponse.builder()
                        .message("Product out of stock!!!")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .data(null)
                        .errors(true)
                        .build();
            }
            CartItem newItem=buildCartItem(item,carts,productRepo);
            newItems.add(newItem);
        }
        if (!newItems.isEmpty()) {
            cartItemRepository.saveAll(newItems);
        }

        List<CartResponse> cartResponses = new ArrayList<>();

        newItems.forEach((item) -> {
            cartResponses.add(buildCartResponse(item.getProductRepo(), item.getQuantity()));
        });

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(cartResponses)
                .message("Update Successfully!")
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
    private CartItem buildCartItem(CartRequest cartRequest, Carts carts, ProductRepo productRepo) {

        return CartItem.builder()
                .cart(carts)
                .quantity(cartRequest.getQuantity())
                .productRepo(productRepo)
                .build();
    }
}
