package com.hoangphi.request.shipping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GHNPostRequest {
    @Builder.Default
    private Integer payment_type_id = 2;
    @Builder.Default
    private String note = "Pet Foster";
    @Builder.Default
    private String required_note = "KHONGCHOXEMHANG";
    @Builder.Default
    private String from_name = "Pet Foster";
    @Builder.Default
    private String from_phone = "0967312797";
    @Builder.Default
    private String from_address = "Khu I đường 3/2, Phường Xuân Khánh, Quận Ninh Kiều, Cần Thơ, Vietnam";
    @Builder.Default
    private String from_ward_name = "Phường Xuân Khánh";
    @Builder.Default
    private String from_district_name = "Quận Ninh Kiều";
    @Builder.Default
    private String from_province_name = "Cần Thơ";
    @Builder.Default
    private String return_phone = "0967312797";
    @Builder.Default
    private String return_address = "Khu I đường 3/2, Phường Xuân Khánh, Quận Ninh Kiều, Cần Thơ, Vietnam";
    @Builder.Default
    private String return_district_id = null;
    @Builder.Default
    private String return_ward_code = "";

    @Builder.Default
    private String client_order_code = "";

    @Builder.Default
    private String to_name = "";
    @Builder.Default
    private String to_phone = "";
    @Builder.Default
    private String to_address = "";
    @Builder.Default
    private String to_ward_code = "";
    private Integer to_district_id;
    @Builder.Default
    private Integer cod_amount = 0;
    @Builder.Default
    private String content = "Pet Foster";
    @Builder.Default
    private Integer weight = 200;

    @Builder.Default
    private Integer pick_station_id = 1444;
    @Builder.Default
    private String deliver_station_id = null;
    @Builder.Default
    private Integer insurance_value = 0;
    @Builder.Default
    private Integer service_id = 0;
    @Builder.Default
    private Integer service_type_id = 2;
    @Builder.Default
    private String coupon = null;
    @Builder.Default
    private ArrayList<String> pick_shift = new ArrayList<>(2);
    private ArrayList<ShippingProductRequest> items;
}
