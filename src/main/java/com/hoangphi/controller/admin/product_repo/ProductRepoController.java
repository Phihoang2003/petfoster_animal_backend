package com.hoangphi.controller.admin.product_repo;

import com.hoangphi.request.variants.CreateRepoRequest;
import com.hoangphi.request.variants.UpdateRepoRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.admin.products_repo.ProductRepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/product-repo")
public class ProductRepoController {
    private final ProductRepoService productRepoService;
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductRepositories(@PathVariable("id") String id) {
        return ResponseEntity.ok(productRepoService.getProductRepositories(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProductRepo(@PathVariable(value = "id") Integer id) {
        System.out.println(id);
        return ResponseEntity.ok(productRepoService.deleteProductRepo(id));
    }

    @PostMapping("/{id}/create")
    public ResponseEntity<ApiResponse> createProductRepo(@PathVariable("id") String id,
                                                         @RequestBody CreateRepoRequest createRepoRequest) {
        return ResponseEntity.ok(productRepoService.addAProductRepository(id, createRepoRequest));

    }
    @PostMapping("{id}/update")
    public ResponseEntity<ApiResponse> updateProductRepo(@PathVariable("id") Integer id,
                                                         @RequestBody UpdateRepoRequest updateRepoRequest, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(productRepoService.updateOrCreateRepo(id, updateRepoRequest, token));
    }
}
