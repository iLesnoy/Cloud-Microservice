package com.petrovskiy.epm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonPropertyOrder({"id", "order-date", "cost", "user", "certificates"})
public class ResponseOrderDto extends RepresentationModel<ResponseOrderDto> {
    @JsonProperty("id")
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("order-date")
    private LocalDateTime orderDate;
    @JsonProperty("cost")
    private BigDecimal cost;
    @JsonProperty("user")
    private UserDto userDto;
    @JsonProperty("certificates")
    private List<GiftCertificateDto> certificateList;
}
