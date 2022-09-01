package com.petrovskiy.epm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;


@Data
@JsonPropertyOrder({"id", "name"})
public class TagDto{
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
}