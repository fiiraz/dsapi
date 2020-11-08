package com.daimontech.dsapi.controller;

import com.daimontech.dsapi.langueages.Repository.LanguageRepository;
import com.daimontech.dsapi.langueages.model.LangueageTable;
import com.daimontech.dsapi.message.request.*;
import com.daimontech.dsapi.message.response.JwtResponse;
import com.daimontech.dsapi.model.*;
import com.daimontech.dsapi.model.enums.Status;
import com.daimontech.dsapi.repository.RoleRepository;
import com.daimontech.dsapi.repository.UserRepository;
import com.daimontech.dsapi.security.jwt.JwtProvider;
import com.daimontech.dsapi.security.services.DiscountUserServiceImpl;
import com.daimontech.dsapi.security.services.UserDetailsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Api(value="Auth islemleri")
public class AuthRestAPIs {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    LanguageRepository languageRepository;

    @Autowired
    DiscountUserServiceImpl discountService;

    @PostMapping("/signin")
    @ApiOperation(value = "Signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

        if(!(userRepository.findStatusByUsername(loginRequest.getUsername()) == Status.ACTIVE)){
            return new ResponseEntity<String>("Fail -> User is not ACTIVE!",
                    HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        boolean ifDelete = userDetailsService.controlIfTokenExistsinDb(jwt, loginRequest.getUsername());
        Optional<User> user = Optional.empty();
        if(ifDelete){
            ActiveUser activeUser = new ActiveUser();
            activeUser.setUsername(loginRequest.getUsername());
            activeUser.setToken(jwt);
            userDetailsService.saveActiveUser(activeUser);
            user = userRepository.findByUsername(loginRequest.getUsername());
        } else {
            user = userRepository.findByUsername(loginRequest.getUsername());
        }
        System.out.println("saving Token into DB");
        return ResponseEntity.ok(new JwtResponse(jwt, user.get()));
    }

    @PostMapping("/signup")
    @ApiOperation(value = "Signup")
    public ResponseEntity<User> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<User>(
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<User>(
                    HttpStatus.BAD_REQUEST);
        }


        // Creating user's account
        signUpRequest.setPassword(encoder.encode(signUpRequest.getPassword()));

        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(signUpRequest, User.class);

        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
        roles.add(userRole);
        user.setRoles(roles);

        user.setStatus(Status.PENDING);

        LangueageTable langueageTable = languageRepository.findByActiveLangueage("EN");

        user.setLangueageTable(langueageTable);
        System.out.println(user.getUsername());
        User userInfo = userRepository.save(user);

        return ResponseEntity.ok().body(userInfo);
    }

    @PostMapping("/signout")
    @ApiOperation(value = "SignOut")
    public ResponseEntity<String> signOut(@Valid @RequestBody SignOutForm signOutForm){
        userDetailsService.deleteByUsername(signOutForm.getUsername());
        return ResponseEntity.ok().body("User sign out successfully!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/newdiscount")
    @ApiOperation(value = "New Discount")
    public ResponseEntity<String> newDiscount(@Valid @RequestBody DiscountUserAddRequest discountUserAddRequest) {
        if (userRepository.existsByUsername(discountUserAddRequest.getUsername())) {
            DiscountUser discountUser = new DiscountUser();
            Optional<User> user = userRepository.findByUsername(discountUserAddRequest.getUsername());
            discountUser.setDiscount(discountUserAddRequest.getDiscount());
            discountUser.setUser(user.get());
            if (discountService.addNewDiscount(discountUser))
                return ResponseEntity.ok().body("Discount created for user successfully!");
        }
        return new ResponseEntity<String>("Fail -> Discount could not be created for user!",
                HttpStatus.BAD_REQUEST);
    }
}