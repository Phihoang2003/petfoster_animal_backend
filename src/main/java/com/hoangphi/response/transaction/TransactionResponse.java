package com.hoangphi.response.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransactionResponse {
    private int id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate when;
    private Double amount;
    private String description;
    private int cumulative_balance;
    private String tid;
    private String subAccId;
    private String bank_sub_acc_id;
    private String bankName;
    private String bankAbbreviation;
    private String virtualAccount;
    private String virtualAccountName;
    private String correspondentName;
    private String correspondentAccount;
    private String correspondentBankId;
    private String correspondentBankName;
}
