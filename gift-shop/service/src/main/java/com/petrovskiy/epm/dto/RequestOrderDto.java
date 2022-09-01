package com.petrovskiy.epm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RequestOrderDto {
    @JsonProperty("user-id")
    private Long userId;
    @JsonProperty("certificates-id")
    private List<Long> certificateIdList;
}