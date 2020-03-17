package com.daimontech.dsapi.controller;

import com.daimontech.dsapi.langueages.Repository.LanguageRepository;
import com.daimontech.dsapi.langueages.model.LangueageTable;
import com.daimontech.dsapi.message.request.LoginForm;
import com.daimontech.dsapi.message.request.SignOutForm;
import com.daimontech.dsapi.message.request.SignUpForm;
import com.daimontech.dsapi.message.response.JwtResponse;
import com.daimontech.dsapi.model.ActiveUser;
import com.daimontech.dsapi.model.Role;
import com.daimontech.dsapi.model.RoleName;
import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.repository.RoleRepository;
import com.daimontech.dsapi.repository.UserRepository;
import com.daimontech.dsapi.security.jwt.JwtProvider;
import com.daimontech.dsapi.security.services.UserDetailsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
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

    @PostMapping("/signin")
    @ApiOperation(value = "Signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        boolean ifDelete = userDetailsService.controlIfTokenExistsinDb(jwt, loginRequest.getUsername());
        if(ifDelete){
            ActiveUser activeUser = new ActiveUser();
            activeUser.setUsername(loginRequest.getUsername());
            activeUser.setToken(jwt);
            userDetailsService.saveActiveUser(activeUser);
        }
        System.out.println("saving Token into DB");
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/signup")
    @ApiOperation(value = "Signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<String>("Fail -> Phone Number is already in use!",
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<String>("Fail -> Email is already in use!",
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));

        //Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        /*strRoles.forEach(role -> {
        	switch(role) {
	    		case "admin":
	    			Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
	                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
	    			roles.add(adminRole);

	    			break;
	    		case "pm":
	            	Role pmRole = roleRepository.findByName(RoleName.ROLE_PM)
	                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
	            	roles.add(pmRole);

	    			break;
	    		default:
	        		Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
	                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
	        		roles.add(userRole);
        	}
        });*/
        Role userRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
        roles.add(userRole);
        user.setRoles(roles);

        LangueageTable langueageTable = languageRepository.findByActiveLangueage("EN");

        user.setLangueageTable(langueageTable);
        userRepository.save(user);

        return ResponseEntity.ok().body("User registered successfully!");
    }

    @PostMapping("/signout")
    @ApiOperation(value = "SignOut")
    public void signOut(@Valid @RequestBody SignOutForm signOutForm){
        userDetailsService.deleteByUsername(signOutForm.getUsername());
    }
}