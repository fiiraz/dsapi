package com.daimontech.dsapi.product.message.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PackagesGetByIdsRequest {
    List<Long> packageIds;
    Long userID;
}
