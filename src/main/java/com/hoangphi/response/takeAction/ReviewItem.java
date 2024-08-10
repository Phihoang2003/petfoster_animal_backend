package com.hoangphi.response.takeAction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewItem {
    private Integer id;

    private String avatar;

    private String name;

    private String displayName;

    private Integer rating;

    private List<Integer> sizes;

    private String comment;

    private String createAt;

    private List<ReviewItem> replyItems;
}
