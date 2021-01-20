package com.daimontech.dsapi.orders.message.response;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
public class OrderedPackagesResponse {

    private Long orderedPackageId;
    private Long packageId;
    private String orderedUserName;
    private Long orderId;
    private int count;

}
