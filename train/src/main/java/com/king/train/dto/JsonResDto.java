package com.king.train.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JsonResDto {

    //private template_list config_str;
    private JsonDetailDto items;
    private String request_id;
    private Boolean success;
    private String template_id;
}
