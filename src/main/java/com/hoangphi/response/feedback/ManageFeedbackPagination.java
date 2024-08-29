package com.hoangphi.response.feedback;

import com.hoangphi.response.common.PaginationResponse;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ManageFeedbackPagination extends PaginationResponse {
    private Integer total;
    public ManageFeedbackPagination(Object data, Integer pages, Integer total) {
        super(data, pages);
        this.total = total;
    }

}
