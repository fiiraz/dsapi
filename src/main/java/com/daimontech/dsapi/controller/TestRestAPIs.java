package com.daimontech.dsapi.controller;

import com.daimontech.dsapi.langueages.Repository.LanguageRepository;
import com.daimontech.dsapi.langueages.model.LangueageTable;
import com.daimontech.dsapi.message.request.FastUserSignUpForm;
import com.daimontech.dsapi.message.request.PMSignUpForm;
import com.daimontech.dsapi.message.request.SignOutForm;
import com.daimontech.dsapi.message.request.VerifyUserForm;
import com.daimontech.dsapi.model.Role;
import com.daimontech.dsapi.model.RoleName;
import com.daimontech.dsapi.model.User;
import com.daimontech.dsapi.model.enums.Status;
import com.daimontech.dsapi.repository.RoleRepository;
import com.daimontech.dsapi.repository.UserRepository;
import com.daimontech.dsapi.security.jwt.JwtProvider;
import com.daimontech.dsapi.security.services.UserDetailsServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/auth2")
public class TestRestAPIs {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	JwtProvider jwtProvider;

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	LanguageRepository languageRepository;

	@Autowired
	PasswordEncoder encoder;
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public String userAccess() {
		return ">>> User Contents!";
	}

	@GetMapping("/pm")
	@PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
	public String projectManagementAccess() {
		return ">>> Board Management Project";
	}
	
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return ">>> Admin Contents";
	}

	@PostMapping("/pmsignup")
	@ApiOperation(value = "PM Signup")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> registerPMUser(@Valid @RequestBody PMSignUpForm pmSignUpForm) {
		if(userRepository.existsByUsername(pmSignUpForm.getUsername())) {
			return new ResponseEntity<String>("Fail -> Phone Number is already in use!",
					HttpStatus.BAD_REQUEST);
		}

		if(userRepository.existsByEmail(pmSignUpForm.getEmail())) {
			return new ResponseEntity<String>("Fail -> Email is already in use!",
					HttpStatus.BAD_REQUEST);
		}


		// Creating user's account
		pmSignUpForm.setPassword(encoder.encode(pmSignUpForm.getPassword()));

		ModelMapper modelMapper = new ModelMapper();
		User user = modelMapper.map(pmSignUpForm, User.class);

		Set<Role> roles = new HashSet<>();

		Role userRole = roleRepository.findByName(RoleName.ROLE_PM)
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
		roles.add(userRole);
		user.setRoles(roles);

		user.setStatus(Status.ACTIVE);

		LangueageTable langueageTable = languageRepository.findByActiveLangueage("EN");

		user.setLangueageTable(langueageTable);
		System.out.println(user.getUsername());
		userRepository.save(user);

		return ResponseEntity.ok().body("PM User registered successfully!");
	}

	@PostMapping("/fastsignup")
	@ApiOperation(value = "Fast User Signup")
	@PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
	public ResponseEntity<String> registerFastUser(@Valid @RequestBody FastUserSignUpForm fastUserSignUpForm) {
		if(userRepository.existsByUsername(fastUserSignUpForm.getUsername())) {
			return new ResponseEntity<String>("Fail -> Phone Number is already in use!",
					HttpStatus.BAD_REQUEST);
		}

		if(userRepository.existsByEmail(fastUserSignUpForm.getEmail())) {
			return new ResponseEntity<String>("Fail -> Email is already in use!",
					HttpStatus.BAD_REQUEST);
		}


		// Creating user's account
		//RANDOM SIFRE OLUSTURULACAK!
		//fastUserSignUpForm.setPassword(encoder.encode(fastUserSignUpForm.getPassword()));

		ModelMapper modelMapper = new ModelMapper();
		User user = modelMapper.map(fastUserSignUpForm, User.class);

		Set<Role> roles = new HashSet<>();

		Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
		roles.add(userRole);
		user.setRoles(roles);

		user.setStatus(Status.ACTIVE);

		LangueageTable langueageTable = languageRepository.findByActiveLangueage("EN");

		user.setLangueageTable(langueageTable);
		System.out.println(user.getUsername());
		userRepository.save(user);
		// BU KISIMDA USER SIFRESI OLUSTURULACAK VE USER'A SIFRESI MAIL ILE GONDERILECEK

		return ResponseEntity.ok().body("PM User registered successfully!");
	}

	@PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
	@PutMapping("/verifyuser")
	@ApiOperation(value = "Verify User")
	public void verifyUser(@Valid @RequestBody VerifyUserForm verifyUserForm){
		Optional<User> user = userRepository.findByUsername(verifyUserForm.getUsername());
		user.get().setStatus(Status.ACTIVE);
		userRepository.save(user.get());
	}

	@PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
	@PutMapping("/blockuser")
	@ApiOperation(value = "Block User")
	public void blockUser(@Valid @RequestBody VerifyUserForm verifyUserForm){
		Optional<User> user = userRepository.findByUsername(verifyUserForm.getUsername());
		user.get().setStatus(Status.INACTIVE);
		userRepository.save(user.get());
	}

	@PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
	@DeleteMapping("/deleteuser")
	@ApiOperation(value = "Delete User")
	public void deleteuser(@Valid @RequestBody VerifyUserForm verifyUserForm){
		if(userRepository.existsByUsername(verifyUserForm.getUsername())){
			Optional<User> user = userRepository.findByUsername(verifyUserForm.getUsername());
			userRepository.delete(user.get());
		}
	}
}