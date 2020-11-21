package com.daimontech.dsapi.orders.message.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
public class OrderUpdateRequest {

    private Long orderId;

    @NotBlank
    @Pattern(regexp ="^\\+(?:[0-9] ?){6,14}[0-9]$")
    private String username;

    private String orderType;

    private List<OrderCountUpdate> orders;
}
