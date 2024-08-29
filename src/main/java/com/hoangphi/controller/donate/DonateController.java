package com.hoangphi.controller.donate;

import com.hoangphi.request.transaction.TransactionBaseRequest;
import com.hoangphi.service.donate.DonateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/donate")
public class DonateController {
    private final DonateService donateService;
    @PostMapping("")
    public ResponseEntity<Object> setTransactionToDB(@RequestBody TransactionBaseRequest transactionBaseRequest) {
        return ResponseEntity.ok(donateService.setTransactionToDB(transactionBaseRequest.getData()));
    }

    @GetMapping("")
    public ResponseEntity<Object> getTransactions(@RequestParam(value = "page") Optional<Integer> page) {
        return ResponseEntity.ok(donateService.getTransaction((page)));
    }


}
