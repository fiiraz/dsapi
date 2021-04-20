package com.daimontech.dsapi.product.controller;

import com.daimontech.dsapi.message.request.VerifyUserForm;
import com.daimontech.dsapi.model.DiscountUser;
import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.product.message.request.*;
import com.daimontech.dsapi.product.message.response.FileUploadResponse;
import com.daimontech.dsapi.product.message.response.PackagePaginationResponse;
import com.daimontech.dsapi.product.model.*;
import com.daimontech.dsapi.product.service.*;
import com.daimontech.dsapi.repository.UserRepository;
import com.daimontech.dsapi.security.services.DiscountUserServiceImpl;
import com.daimontech.dsapi.utilities.error.BaseError;
import com.daimontech.dsapi.utilities.helpers.LanguageSwitch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/package")
@Api(value = "Package islemleri")
public class PackageController {

    @Autowired
    PackageServiceImpl packageService;

    @Autowired
    CategoriesServiceImpl categoriesService;

    @Autowired
    ColorServiceImpl colorService;

    @Autowired
    BaseError baseError;

    @Autowired
    LanguageSwitch languageSwitch;

    @Autowired
    FileUploadServiceImpl fileUploadService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRateServiceImpl productRateService;

    @Autowired
    DiscountServiceImpl discountPackageService;

    @Autowired
    DiscountUserServiceImpl discountUserService;

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @PostMapping("/newpackage")
    @ApiOperation(value = "New Package")
    public ResponseEntity<String> newPackage(@Valid @RequestBody PackageAddRequest packageAddRequest) {
        languageSwitch.setLang();

        ModelMapper modelMapper = new ModelMapper();
        //Properties properties = modelMapper.map(packageAddRequest.getPropertyAddRequest(), Properties.class);
/*        if(!propertyService.addNewProperty(properties)){
            return new ResponseEntity<String>(baseError.errorMap.get(baseError.getSexUnsaved()),
                    HttpStatus.BAD_REQUEST);
        }*/
        Packages packages = new Packages();
        packages.setTitle(packageAddRequest.getTitle());
        packages.setPatternCode(packageAddRequest.getPatternCode());
        packages.setAsortiCode(packageAddRequest.getAsortiCode());
        packages.setSizeMin(packageAddRequest.getSizeMin());
        packages.setSizeMax(packageAddRequest.getSizeMax());
        packages.setDescription(packageAddRequest.getDescription());
        packages.setCategories(categoriesService.getCategoryById(packageAddRequest.getCategoryId()));
        packages.setProductCode(packageAddRequest.getProductCode());
        packages.setPrice(packageAddRequest.getPrice());
        packages.setImagesPath(packageAddRequest.getImagesPath());
        packages.setCreatedDate(new Date());
        packages.setForRateOnly(packageAddRequest.getForRateOnly());
        packages.setRateAllowed(packageAddRequest.getRateAllowed());
        packages.setAimCountries(packageAddRequest.getAimCountries());

        Set<Colors> colorList = new HashSet<>();
        for (Long color : packageAddRequest.getColorId()
        ) {
            colorList.add(colorService.getColorsById(color));
        }
        packages.setColors(colorList);
        if (!packageService.addNewPackage(packages)) {
            return new ResponseEntity<String>("Fail -> Package could not be added!",
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body("Package added successfully!");
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @DeleteMapping("/deletepackage")
    @ApiOperation(value = "Delete Package")
    public ResponseEntity<String> deletePackage(@Valid @RequestParam long packageId) {
        if (packageService.existsByPackageId(packageId)) {
            Packages packages = packageService.getByPackageId(packageId);
            if (packageService.delete(packages))
                return ResponseEntity.ok().body("Package deleted successfully!");
        }
        return new ResponseEntity<String>("Fail -> Package could not be deleted!",
                HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @PutMapping("/editpackage")
    @ApiOperation(value = "Edit Package")
    public ResponseEntity<String> editPackage(@Valid @RequestBody EditPackageRequest editPackageRequest) {
        if (packageService.existsByPackageId(editPackageRequest.getId())) {
            Packages packages = packageService.getByPackageId(editPackageRequest.getId());
            ModelMapper modelMapper = new ModelMapper();
            Packages packages1 = modelMapper.map(editPackageRequest, Packages.class);
            packages1.setCreatedDate(packages.getCreatedDate());
            Categories categories = categoriesService.getCategoryById(editPackageRequest.getCategoryId());
            packages1.setCategories(categories);
            packages1.setForRateOnly(editPackageRequest.getForRateOnly());
            packages1.setRateAllowed(editPackageRequest.getRateAllowed());
            packages1.setAimCountries(editPackageRequest.getAimCountries());
            Set<Colors> colorList = new HashSet<>();
            for (Long color : editPackageRequest.getColorId()
            ) {
                colorList.add(colorService.getColorsById(color));
            }
            packages1.setColors(colorList);
            if (packageService.addNewPackage(packages1))
                return ResponseEntity.ok().body("Package edited successfully!");
        }
        return new ResponseEntity<String>("Fail -> Package could not be edited!",
                HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/page/{pageNo}/{sortingValue}/{searchingValue}/{forRateValue}/{userID}/{pageSize}")
    @ApiOperation(value = "Package User")
    public ResponseEntity<List<PackagePaginationResponse>> getPackagesPaginated(@Valid @PathVariable(value = "pageNo") int pageNo,
    @PathVariable(value = "sortingValue", required = false) String sortingValue,
    @PathVariable(value = "searchingValue", required = false) Optional<String> searchingValue,
    @PathVariable(value = "forRateValue", required = false) Optional<Boolean> forRate,
    @PathVariable(value = "userID", required = false) Optional<Long> userID,
    @PathVariable(value = "pageSize", required = false) Optional<Integer> pageSize) {
        Page<Packages> page;
        if (!forRate.get()) {
            page = packageService.findPaginated(pageNo, pageSize.get(), sortingValue, searchingValue.orElse("_"), false);
        } else {
            page = packageService.findPaginated(pageNo, pageSize.get(), sortingValue, searchingValue.orElse("_"), true);
        }
        List<Packages> listPackages = page.getContent();
        List<PackagePaginationResponse> pagedPackages = new ArrayList<>();
        Optional<User> user = userRepository.findById(userID.get());
        List<DiscountUser> discountUserList = discountUserService.getAllByUser(user.get());
        for(Packages packages : listPackages){
            PackagePaginationResponse packagePaginationResponse = new PackagePaginationResponse();
            packagePaginationResponse.setTitle(packages.getTitle());
            packagePaginationResponse.setProductCode(packages.getProductCode());
            packagePaginationResponse.setDescription(packages.getDescription());
            packagePaginationResponse.setPatternCode(packages.getPatternCode());
            packagePaginationResponse.setAsortiCode(packages.getAsortiCode());
            packagePaginationResponse.setSizeMin(packages.getSizeMin());
            packagePaginationResponse.setSizeMax(packages.getSizeMax());
            packagePaginationResponse.setId(packages.getId());
            packagePaginationResponse.setPrice(packages.getPrice());
            packagePaginationResponse.setAimCountries(packages.getAimCountries());
            List<DiscountPackage> discountPackageList = discountPackageService.getAllByPackage(packages);
            double packagePrice = packages.getPrice();
            if (discountPackageList.size() != 0) {
                for (DiscountPackage discountPackage :
                        discountPackageList
                ) {
                    packagePrice -= packagePrice * (discountPackage.getDiscount() / 100);
                }
            }
            if (discountUserList.size() != 0) {
                for (DiscountUser discountUser :
                        discountUserList
                ) {
                    packagePrice -= packagePrice * (discountUser.getDiscount() / 100);
                }
            }
            packagePaginationResponse.setDiscountPrice(packagePrice);
            packagePaginationResponse.setCategoryId(packages.getCategories().getId());
            packagePaginationResponse.setCategoryName(packages.getCategories().getCategoryName());
            packagePaginationResponse.setCategoryParent(packages.getCategories().getParent());
            packagePaginationResponse.setColorsList(packages.getColors());
            packagePaginationResponse.setImagesPath(packages.getImagesPath());
            packagePaginationResponse.setCreatedDate(new Date());
            packagePaginationResponse.setForRateOnly(packages.getForRateOnly());
            packagePaginationResponse.setRateAllowed(packages.getRateAllowed());
            Optional<ProductRate> productRate = productRateService.findByUserIdAndPackages(userID.get(), packages);

            if (forRate.get()) {
                String country = user.get().getCountry();
                if(packages.getAimCountries().contains(country) && !productRate.isPresent()){
                    packagePaginationResponse.setRate(0);
                    pagedPackages.add(packagePaginationResponse);
                }
            } else {
                if (productRate.isPresent()) {
                    packagePaginationResponse.setRate(productRate.get().getRate());
                } else {
                    packagePaginationResponse.setRate(0);
                }
                pagedPackages.add(packagePaginationResponse);
            }
        }
        if (!page.isEmpty()) {
            return ResponseEntity.ok().body(pagedPackages);
        }
        return new ResponseEntity<>(
                HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getpackage/{id}")
    @ApiOperation(value = "Package User")
    public ResponseEntity<Optional<Packages>> getPackageById(@Valid @PathVariable(value = "id") Long packageId) {
        Optional<Packages> packages = packageService.findOneByPackageId(packageId);

            return ResponseEntity.ok().body(packages);

    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getpackagerate/{id}/{packageID}")
    @ApiOperation(value = "Package rate by User")
    public ResponseEntity<Optional<ProductRate>> getPackageRateByUserId(@Valid @PathVariable(value = "id") Long userID,
                                                                        @PathVariable(value = "packageID") Long packageID) {
        Packages packages = packageService.getByPackageId(packageID);
        Optional<ProductRate> productRate = productRateService.findByUserIdAndPackages(userID, packages);

        return ResponseEntity.ok().body(productRate);

    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/ratepackage")
    @ApiOperation(value = "Package Rate")
    public ResponseEntity<ProductRate> userRatePackage(@Valid @RequestBody UserRatePackageRequest userRatePackageRequest) {
        Optional<Packages> packages = packageService.findOneByPackageId(userRatePackageRequest.getPackageID());
        Optional<User> user = userRepository.findByUsername(userRatePackageRequest.getUsername());
        ProductRate productRate = new ProductRate();
        productRate.setUser(user.get());
        productRate.setPackages(packages.get());
        productRate.setRate(userRatePackageRequest.getRate());

        productRateService.addNewProductRate(productRate);

        return ResponseEntity.ok().body(productRate);

    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @PutMapping("/rateupdatepackage")
    @ApiOperation(value = "Update Package Rate")
    public ResponseEntity<String> updateUserRatePackage(@Valid @RequestBody UpdateUserRatePackageRequest updateUserRatePackageRequest) {
        Optional<ProductRate> productRate = productRateService.findOneByPackageId(updateUserRatePackageRequest.getProductRateID());
        productRateService.addNewProductRate(productRate.get());

        return ResponseEntity.ok().body("Rate changed successfully!");

    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @DeleteMapping("/ratedeletepackage")
    @ApiOperation(value = "Delete Package Rate")
    public ResponseEntity<String> deleteUserRatePackage(@Valid @RequestParam long ratePackageID) {
        Optional<ProductRate> productRate = productRateService.findOneByPackageId(ratePackageID);
        productRateService.delete(productRate.get());

        return ResponseEntity.ok().body("Rate deleted successfully!");

    }

    /*@PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/uploadimages/{id}")
    public ResponseEntity<List<FileUploadResponse>> uploadDb(@Valid @PathVariable(value = "id") Long packageId,
                                       @RequestParam("file") List<MultipartFile> multipartFile) throws IOException {
        List<FileUploadResponse> images = new ArrayList<>();
        for (MultipartFile file : multipartFile) {
            Images image = new Images();
            image.setData(file.getBytes());
            image.setName(file.getOriginalFilename());
            image.setType(file.getContentType());
            Images uploadedFile = fileUploadService.uploadToDb(image);
            FileUploadResponse response = new FileUploadResponse();
            if(uploadedFile!=null){
                String downloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/download/")
                        .path(uploadedFile.getName())
                        .toUriString();
                response.setUrl(downloadUri);
                response.setName(uploadedFile.getName());
                response.setType(uploadedFile.getType());
                response.setSize(uploadedFile.getData().length);
                response.setMessage("Image Uploaded Successfully");
                images.add(response);
            }
        }
        if(!images.isEmpty()) {
            return ResponseEntity.ok().body(images);
        }
        return new ResponseEntity<>(
                HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getimage/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String id)
    {
        Images uploadedFileToRet =  fileUploadService.downloadFile(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(uploadedFileToRet.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename= "+uploadedFileToRet.getName())
                .body(new ByteArrayResource(uploadedFileToRet.getData()));
    }

    //TEST IMAGE
    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getallimages")
    public ResponseEntity<List<FileUploadResponse>> getListFiles() {
        List<FileUploadResponse> files = fileUploadService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/download/")
                    .path(dbFile.getId())
                    .toUriString();

            return new FileUploadResponse(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    "images",
                    dbFile.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getimage2/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String imageId) {
        Images fileDB = fileUploadService.getFile(imageId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }*/
}
