package com.daimontech.dsapi.product.service;

import com.daimontech.dsapi.product.message.response.PackagePaginationResponse;
import com.daimontech.dsapi.product.model.Packages;
import org.springframework.data.domain.Page;

public interface PackageService {

    Page<Packages> findPaginated(int pageNo, int pageSize, String sortingValue);
}
