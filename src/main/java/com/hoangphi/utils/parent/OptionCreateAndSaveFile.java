package com.hoangphi.utils.parent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionCreateAndSaveFile {
    private List<String> acceptExtentions;
    private String defaultExtention;

    public String getDefaultExtention(){
        if(defaultExtention==null||defaultExtention.isBlank()){
            return "jpg";
        }
        return defaultExtention;
    }
}
