package com.hoangphi.controller.donate;

import com.hoangphi.request.transaction.TransactionBaseRequest;
import com.hoangphi.service.donate.DonateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/donate")
public class DonateController {
    private final DonateService donateService;
    @PostMapping("")
    public ResponseEntity<Object> setTransactionToDB(@RequestBody TransactionBaseRequest transactionBaseRequest) {
        return ResponseEntity.ok(donateService.setTransactionToDB(transactionBaseRequest.getData()));
    }
}
