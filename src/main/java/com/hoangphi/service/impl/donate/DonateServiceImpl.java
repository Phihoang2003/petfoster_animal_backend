package com.hoangphi.service.impl.donate;

import com.hoangphi.entity.Donate;
import com.hoangphi.repository.DonateRepository;
import com.hoangphi.request.transaction.TransactionRequest;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.transaction.TransactionResponse;
import com.hoangphi.service.donate.DonateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DonateServiceImpl implements DonateService {
    private final DonateRepository donateRepository;
    @Override
    public ApiResponse getTransaction(Optional<Integer> page) {
        return null;
    }

    @Override
    public ApiResponse report() {
        return null;
    }

    @Override
    public ApiResponse filterTransaction(Optional<String> search, Optional<Date> minDate, Optional<Date> maxDate, Optional<String> sort, Optional<Integer> page) {
        return null;
    }

    @Override
    public ApiResponse setTransactionToDB(List<TransactionRequest> transactionRequest) {
        if (transactionRequest == null) {
            return ApiResponse.builder()
                    .message("Failure !")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .errors(true)
                    .build();

        }

        List<Donate> donates = this.buildDonates(transactionRequest);

        if (donates == null || donates.isEmpty()) {
            return ApiResponse.builder()
                    .message("Failure !")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .errors(true)
                    .build();
        }

        donateRepository.saveAll(donates);

        return ApiResponse.builder()
                .message("Successfully !")
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(this.buildResponses(transactionRequest))
                .build();
    }
    public Donate builDonate(TransactionRequest transactionRequest) {
        return Donate.builder()
                .descriptions(transactionRequest.getDescription())
                .donateAmount(transactionRequest.getAmount())
                .donateAt(transactionRequest.getWhen())
                .donater(transactionRequest.getCorrespondentAccount() == null
                        || transactionRequest.getCorrespondentAccount().isBlank() ? "Incognito"
                        : transactionRequest.getCorrespondentAccount())
                .beneficiaryBank(transactionRequest.getBankName())
                .toAccountNumber(transactionRequest.getSubAccId())
                .idTransaction(transactionRequest.getId())
                .build();
    }
    public List<Donate> buildDonates(List<TransactionRequest> transactionRequests) {

        return transactionRequests.stream().map(this::builDonate).toList();
    }
    public List<TransactionResponse> buildResponses(List<TransactionRequest> transactionRequests) {

        return transactionRequests.stream().map(this::buildResponse).toList();
    }
    public TransactionResponse buildResponse(TransactionRequest transactionRequest) {
        return TransactionResponse.builder()
                .id(transactionRequest.getId())
                .amount(transactionRequest.getAmount())
                .bank_sub_acc_id(transactionRequest.getBank_sub_acc_id())
                .correspondentAccount(transactionRequest.getCorrespondentAccount())
                .correspondentBankId(transactionRequest.getCorrespondentBankId())
                .correspondentBankName(transactionRequest.getCorrespondentBankName())
                .correspondentName(transactionRequest.getCorrespondentName())
                .cumulative_balance(transactionRequest.getCumulative_balance())
                .description(transactionRequest.getDescription())
                .tid(transactionRequest.getTid())
                .virtualAccount(transactionRequest.getVirtualAccount())
                .virtualAccountName(transactionRequest.getVirtualAccountName())
                .build();
    }

}
