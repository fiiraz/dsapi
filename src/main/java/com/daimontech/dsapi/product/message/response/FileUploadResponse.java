package com.daimontech.dsapi.product.message.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadResponse {
    private String name;
    private String url;
    private String type;
    private String message;
    private long size;
    private String data;
}
