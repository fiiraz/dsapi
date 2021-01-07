package com.daimontech.dsapi.recommendedPackage.controller;

import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.product.message.request.PackageAddRequest;
import com.daimontech.dsapi.product.message.response.PackagePaginationResponse;
import com.daimontech.dsapi.product.model.Colors;
import com.daimontech.dsapi.product.model.Packages;
import com.daimontech.dsapi.recommendedPackage.message.request.RecommendedPackageRateRequest;
import com.daimontech.dsapi.recommendedPackage.message.request.RecommendedPackagesAddRequest;
import com.daimontech.dsapi.recommendedPackage.message.request.RecommendedPackagesUpdateRequest;
import com.daimontech.dsapi.recommendedPackage.message.response.RecommendedPackagePaginationResponse;
import com.daimontech.dsapi.recommendedPackage.model.RecommendedNewPackages;
import com.daimontech.dsapi.recommendedPackage.model.RecommendedNewPackagesRate;
import com.daimontech.dsapi.recommendedPackage.service.RecommendedPackagesService;
import com.daimontech.dsapi.recommendedPackage.service.RecommendedPackagesServiceImpl;
import com.daimontech.dsapi.repository.UserRepository;
import com.daimontech.dsapi.utilities.error.BaseError;
import com.daimontech.dsapi.utilities.helpers.LanguageSwitch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recommendedpackage")
@Api(value = "Recommended Package islemleri")
public class RecommendedPackagesController {

    @Autowired
    RecommendedPackagesServiceImpl recommendedPackagesService;

    @Autowired
    BaseError baseError;

    @Autowired
    LanguageSwitch languageSwitch;

    @Autowired
    UserRepository userRepository;

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @PostMapping("/newrecommendedpackage")
    @ApiOperation(value = "New Recommended Package")
    public ResponseEntity<String> newRecommendedPackage(@Valid @RequestBody
                                                                RecommendedPackagesAddRequest recommendedPackagesAddRequest) {
        languageSwitch.setLang();

        ModelMapper modelMapper = new ModelMapper();
        RecommendedNewPackages recommendedNewPackages = modelMapper.map(
                recommendedPackagesAddRequest,
                RecommendedNewPackages.class);
        recommendedNewPackages.setReleaseDate(new Date());
        recommendedNewPackages.setStatus(true);
        if (!recommendedPackagesService.addNewRecommendedPackage(recommendedNewPackages)) {
            return new ResponseEntity<String>(baseError.errorMap.get(baseError.getSexUnsaved()),
                    HttpStatus.BAD_REQUEST);
        }

        if (recommendedPackagesService.addNewRecommendedPackage(recommendedNewPackages)) {
            return ResponseEntity.status(HttpStatus.OK).body(baseError.errorMap.get(baseError.getSexSaved()));
        }

        return new ResponseEntity<String>(baseError.errorMap.get(baseError.getUnknownError()),
                HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
    @PutMapping("/updaterecommendedpackage")
    @ApiOperation(value = "Update Recommended Package")
    public ResponseEntity<String> updateRecommendedPackage(
            @Valid @RequestBody
                    RecommendedPackagesUpdateRequest recommendedPackagesUpdateRequest) {
        languageSwitch.setLang();
        Optional<RecommendedNewPackages> recommendedNewPackages;
        try {
            recommendedNewPackages = recommendedPackagesService.findById(
                    recommendedPackagesUpdateRequest.getId().longValue());
        } catch (Exception e) {
            return new ResponseEntity<String>("Package is not exist.",
                    HttpStatus.BAD_REQUEST);
        }
        recommendedNewPackages.get().setStatus(recommendedPackagesUpdateRequest.getStatus());
        recommendedNewPackages.get().setTitle(recommendedPackagesUpdateRequest.getTitle());
        recommendedNewPackages.get().setReleaseDate(new Date());
        recommendedNewPackages.get().setAimCountry(recommendedPackagesUpdateRequest.getAimCountry());
        recommendedNewPackages.get().setDescription(recommendedPackagesUpdateRequest.getDescription());
        recommendedNewPackages.get().setPatternCode(recommendedPackagesUpdateRequest.getPatternCode());
        recommendedNewPackages.get().setSizeMax(recommendedPackagesUpdateRequest.getSizeMax());
        recommendedNewPackages.get().setSizeMin(recommendedPackagesUpdateRequest.getSizeMin());
        if (!recommendedPackagesService.updateRecommendedPackage(recommendedNewPackages.get())) {
            return new ResponseEntity<String>(baseError.errorMap.get(baseError.getSexUnsaved()),
                    HttpStatus.BAD_REQUEST);
        }

        if (recommendedPackagesService.updateRecommendedPackage(recommendedNewPackages.get())) {
            return ResponseEntity.status(HttpStatus.OK).body(baseError.errorMap.get(baseError.getSexSaved()));
        }

        return new ResponseEntity<String>(baseError.errorMap.get(baseError.getUnknownError()),
                HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/newrecommendedpackageRate")
    @ApiOperation(value = "Recommended Package Rate")
    public ResponseEntity<String> rateRecommendedPackage(@Valid @RequestBody
                                                                 RecommendedPackageRateRequest recommendedPackageRateRequest) {
        languageSwitch.setLang();

        RecommendedNewPackagesRate recommendedNewPackagesRate = new RecommendedNewPackagesRate();
        Optional<User> user = userRepository.findById(recommendedPackageRateRequest.getUserId().longValue());
        User ratedUser = user.get();
        Optional<RecommendedNewPackages> recommendedNewPackages = recommendedPackagesService.findById(
                recommendedPackageRateRequest.getPackageId().longValue());
        RecommendedNewPackages ratedRecommendedPackage = recommendedNewPackages.get();
        recommendedNewPackagesRate.setComment(recommendedPackageRateRequest.getComment());
        recommendedNewPackagesRate.setRate(recommendedPackageRateRequest.getRate());
        recommendedNewPackagesRate.setRecommendedNewPackages(ratedRecommendedPackage);
        recommendedNewPackagesRate.setUser(ratedUser);
        recommendedNewPackagesRate.setRatedDate(new Date());
        if (recommendedPackagesService.isRatedBeforeByUser(ratedUser, ratedRecommendedPackage)) {
            recommendedPackagesService.updateRate(
                    recommendedNewPackagesRate.getComment(),
                    recommendedNewPackagesRate.getRate(),
                    recommendedNewPackagesRate.getRatedDate(),
                    recommendedPackageRateRequest.getUserId().longValue(),
                    recommendedPackageRateRequest.getPackageId().longValue());
            return ResponseEntity.status(HttpStatus.OK).body(baseError.errorMap.get(baseError.getSexSaved()));

        } else {
            if (!recommendedPackagesService.addNewRateForRecommendedPackace(recommendedNewPackagesRate)) {
                return new ResponseEntity<String>(baseError.errorMap.get(baseError.getSexUnsaved()),
                        HttpStatus.BAD_REQUEST);
            }

            if (recommendedPackagesService.addNewRateForRecommendedPackace(recommendedNewPackagesRate)) {
                return ResponseEntity.status(HttpStatus.OK).body(baseError.errorMap.get(baseError.getSexSaved()));
            }
        }

        return new ResponseEntity<String>(baseError.errorMap.get(baseError.getUnknownError()),
                HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getonrrecommendedpackageRate/{id}")
    @ApiOperation(value = "get Recommended Package by id")
    public ResponseEntity<Optional<RecommendedNewPackages>> getRecommendedPackageById(@Valid @PathVariable(value = "id") Long recommendedPackageId) {
        Optional<RecommendedNewPackages> recommendedNewPackages = recommendedPackagesService.findOneByPackageId(recommendedPackageId);

        return ResponseEntity.ok().body(recommendedNewPackages);

    }

    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/page/{pageNo}/{sortingValue}")
    @ApiOperation(value = "get All Recommended Packages Paginated")
    public ResponseEntity<List<RecommendedPackagePaginationResponse>> getRecommendedPackagesPaginated(@Valid @PathVariable(value = "pageNo") int pageNo,
                                                                                                      @PathVariable(value = "sortingValue", required = false) String sortingValue) {
        int pageSize = 2;
        Page<RecommendedNewPackages> page = recommendedPackagesService.findPaginated(pageNo, pageSize, sortingValue);
        List<RecommendedNewPackages> listRecommendedPackages = page.getContent();
        List<RecommendedPackagePaginationResponse> pagedRecommendedPackages = new ArrayList<>();
        for(RecommendedNewPackages packages : listRecommendedPackages){
            RecommendedPackagePaginationResponse packagePaginationResponse = new RecommendedPackagePaginationResponse();
            packagePaginationResponse.setAimCountry(packages.getAimCountry());
            packagePaginationResponse.setTitle(packages.getTitle());
            packagePaginationResponse.setDescription(packages.getDescription());
            packagePaginationResponse.setPatternCode(packages.getPatternCode());
            packagePaginationResponse.setReleaseDate(packages.getReleaseDate());
            packagePaginationResponse.setSizeMax(packages.getSizeMax());
            packagePaginationResponse.setSizeMin(packages.getSizeMin());
            packagePaginationResponse.setStatus(packages.getStatus());
            packagePaginationResponse.setId(packages.getId());
            packagePaginationResponse.setReleaseDate(new Date());
            pagedRecommendedPackages.add(packagePaginationResponse);
        }
        if (!page.isEmpty()) {
            return ResponseEntity.ok().body(pagedRecommendedPackages);
        }
        return new ResponseEntity<>(
                HttpStatus.NOT_FOUND);
    }

}
