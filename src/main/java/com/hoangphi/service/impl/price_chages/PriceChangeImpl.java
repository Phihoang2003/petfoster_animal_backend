package com.hoangphi.service.impl.price_chages;

import com.hoangphi.constant.RespMessage;
import com.hoangphi.entity.PriceChange;
import com.hoangphi.repository.PriceChangeRepository;
import com.hoangphi.response.ApiResponse;
import com.hoangphi.response.price_changes.PriceChangeResponse;
import com.hoangphi.service.admin.price_changes.PriceChangeService;
import com.hoangphi.utils.PortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PriceChangeImpl implements PriceChangeService {
    private final PriceChangeRepository priceChangeRepository;
    private final PortUtils portUtils;
    @Override
    public ApiResponse getPriceChange(String idProduct) {

        if (idProduct.isBlank()) {
            return ApiResponse.builder()
                    .message("Product not found !!!")
                    .status(412)
                    .errors(true)
                    .data(null)
                    .build();
        }

        List<PriceChange> priceChanges = priceChangeRepository.findByProductId(idProduct);

        List<PriceChangeResponse> priceChangeResponses = new ArrayList<>();

        if (!priceChanges.isEmpty()) {
            priceChanges.forEach(item -> {
                priceChangeResponses.add(buildPriceChangeResponse(item));
            });
        }

        return ApiResponse.builder()
                .message(RespMessage.SUCCESS.getValue())
                .status(HttpStatus.OK.value())
                .errors(false)
                .data(priceChangeResponses)
                .build();
    }

    private PriceChangeResponse buildPriceChangeResponse(PriceChange priceChange) {
        Map<String, String> user = new HashMap<>();

        user.put("id", priceChange.getUser().getId());
        user.put("fullname", priceChange.getUser().getFullname() == null ? priceChange.getUser().getUsername()
                : priceChange.getUser().getFullname());
        user.put("avartar", portUtils.getUrlImage(priceChange.getUser().getAvatar()));

        return PriceChangeResponse.builder()
                .id(priceChange.getId())
                .newInPrice(priceChange.getNewInPrice())
                .newOutPrice(priceChange.getNewOutPrice())
                .oldInPrice(priceChange.getOldInPrice())
                .oldOutPrice(priceChange.getOldOutPrice())
                .updateAt(priceChange.getUpdateAt())
                .size(priceChange.getProductRepo().getSize())
                .user(user)
                .build();
    }
}
