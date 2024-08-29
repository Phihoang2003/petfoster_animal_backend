package com.hoangphi.controller.admin.user;

import com.hoangphi.response.ApiResponse;
import com.hoangphi.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;
    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllUser(@RequestHeader("Authorization") String jwt,
                                                  @RequestParam("keyword") Optional<String> keyword,
                                                  @RequestParam("sort") Optional<String> sort,
                                                  @RequestParam("page") Optional<Integer> pages,
                                                  @RequestParam("role") Optional<String> roles) {
        return ResponseEntity.ok(userService.getAllUser(jwt, keyword, sort, roles, pages));
    }
}
