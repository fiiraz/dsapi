package com.daimontech.dsapi.product.controller;

import com.daimontech.dsapi.message.request.VerifyUserForm;
import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.product.message.request.CategoryAddRequest;
import com.daimontech.dsapi.product.message.request.EditPackageRequest;
import com.daimontech.dsapi.product.message.request.PackageAddRequest;
import com.daimontech.dsapi.product.message.request.PackageDeleteRequest;
import com.daimontech.dsapi.product.message.response.FileUploadResponse;
import com.daimontech.dsapi.product.message.response.PackagePaginationResponse;
import com.daimontech.dsapi.product.model.*;
import com.daimontech.dsapi.product.service.*;
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
    @GetMapping("/page/{pageNo}/{sortingValue}/{searchingValue}")
    @ApiOperation(value = "Package User")
    public ResponseEntity<List<PackagePaginationResponse>> getPackagesPaginated(@Valid @PathVariable(value = "pageNo") int pageNo,
    @PathVariable(value = "sortingValue", required = false) String sortingValue,
    @PathVariable(value = "searchingValue", required = false) Optional<String> searchingValue) {
        int pageSize = 2;
        Page<Packages> page = packageService.findPaginated(pageNo, pageSize, sortingValue, searchingValue.orElse("_"));
        List<Packages> listPackages = page.getContent();
        List<PackagePaginationResponse> pagedPackages = new ArrayList<>();
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
            packagePaginationResponse.setCategoryId(packages.getCategories().getId());
            packagePaginationResponse.setCategoryName(packages.getCategories().getCategoryName());
            packagePaginationResponse.setCategoryParent(packages.getCategories().getParent());
            packagePaginationResponse.setColorsList(packages.getColors());
            packagePaginationResponse.setImagesPath(packages.getImagesPath());
            packagePaginationResponse.setCreatedDate(new Date());
            pagedPackages.add(packagePaginationResponse);
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
