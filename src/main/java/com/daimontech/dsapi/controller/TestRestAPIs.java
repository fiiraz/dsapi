package com.daimontech.dsapi.controller;

import com.daimontech.dsapi.langueages.Repository.LanguageRepository;
import com.daimontech.dsapi.langueages.model.LangueageTable;
import com.daimontech.dsapi.message.request.*;
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
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
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

//		user.setLangueageTable(langueageTable);
		System.out.println(user.getUsername());
		userRepository.save(user);

		return ResponseEntity.ok().body("PM User registered successfully!");
	}

	@PostMapping("/fastsignup")
	@ApiOperation(value = "Fast User Signup")
	@PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
	public ResponseEntity<User> registerFastUser(@Valid @RequestBody FastUserSignUpForm fastUserSignUpForm) {
		if(userRepository.existsByUsername(fastUserSignUpForm.getUsername())) {
			return new ResponseEntity<User>(
					HttpStatus.BAD_REQUEST);
		}

		if(userRepository.existsByEmail(fastUserSignUpForm.getEmail())) {
			return new ResponseEntity<User>(
					HttpStatus.BAD_REQUEST);
		}


		// Creating user's account
		//RANDOM SIFRE OLUSTURULACAK!

		ModelMapper modelMapper = new ModelMapper();
		User user = modelMapper.map(fastUserSignUpForm, User.class);

		user.setPassword(encoder.encode("12345"));

		Set<Role> roles = new HashSet<>();

		Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
		roles.add(userRole);
		user.setRoles(roles);

		user.setStatus(Status.ACTIVE);

		LangueageTable langueageTable = languageRepository.findByActiveLangueage("EN");

//		user.setLangueageTable(langueageTable);
		System.out.println(user.getUsername());
		User userInfo = userRepository.save(user);
		// BU KISIMDA USER SIFRESI OLUSTURULACAK VE USER'A SIFRESI MAIL ILE GONDERILECEK

		return ResponseEntity.ok().body(userInfo);
	}

	@PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
	@PutMapping("/verifyuser")
	@ApiOperation(value = "Verify User")
	public ResponseEntity<String> verifyUser(@Valid @RequestBody VerifyUserForm verifyUserForm) {
		Optional<User> user = userRepository.findByUsername(verifyUserForm.getUsername());
		if (user.isPresent()) {
			user.get().setStatus(Status.ACTIVE);
			userRepository.save(user.get());
			return ResponseEntity.ok().body("User verified successfully!");
		}
		return new ResponseEntity<String>("Fail -> User could not be verified!",
				HttpStatus.BAD_REQUEST);
	}

	@PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
	@PutMapping("/forgotpassword")
	@ApiOperation(value = "Forgot Password")
	public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
		Optional<User> user = userRepository.findByEmail(forgotPasswordRequest.getEmail());
		if (user.isPresent()) {
			String generatedPassword = generateRandomPassword(6);
			//send by email after creating new password.
			user.get().setPassword(encoder.encode(generatedPassword));
			userRepository.save(user.get());
			return ResponseEntity.ok().body("Password changed successfully and sent a mail!");
		}
		return new ResponseEntity<String>("Fail -> Password could not be changed!",
				HttpStatus.BAD_REQUEST);
	}

	@PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
	@PutMapping("/changepassword")
	@ApiOperation(value = "Change Password")
	public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
		Optional<User> user = userRepository.findByUsername(changePasswordRequest.getUsername());
		if (user.isPresent()) {
			if (encoder.encode(changePasswordRequest.getOldPassword()).equals(user.get().getPassword())) {
				return new ResponseEntity<String>("Fail -> Password is wrong!",
						HttpStatus.BAD_REQUEST);
			}
			if (encoder.encode(changePasswordRequest.getOldPassword()).equals(encoder.encode(changePasswordRequest.getNewPassword()))) {
				return new ResponseEntity<String>("Fail -> New Password can not be the same with Old Password!",
						HttpStatus.BAD_REQUEST);
			}
			user.get().setPassword(encoder.encode(changePasswordRequest.getNewPassword()));
			userRepository.save(user.get());
			return ResponseEntity.ok().body("Password changed successfully!");
		}
		return new ResponseEntity<String>("Fail -> Password could not be changed!",
				HttpStatus.BAD_REQUEST);
	}

	@PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
	@PutMapping("/edituser")
	@ApiOperation(value = "edit User")
	public ResponseEntity<String> editUser(@Valid @RequestBody EditUserForm editUserForm){
		try {
			Optional<User> user = userRepository.findByUsername(editUserForm.getUsername());
			ModelMapper modelMapper = new ModelMapper();
			User editedUser = modelMapper.map(editUserForm, User.class);
			userRepository.save(editedUser);
			return ResponseEntity.ok().body("User edited successfully!");
		} catch (Exception e) {
			return new ResponseEntity<String>("Fail -> User could not be edited!",
					HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
	@PutMapping("/blockuser")
	@ApiOperation(value = "Block User")
	public ResponseEntity<String> blockUser(@Valid @RequestBody VerifyUserForm verifyUserForm){
		Optional<User> user = userRepository.findByUsername(verifyUserForm.getUsername());
		if(user.isPresent()) {
			user.get().setStatus(Status.INACTIVE);
			userRepository.save(user.get());
			return ResponseEntity.ok().body("User blocked successfully!");
		}
		return new ResponseEntity<String>("Fail -> User could not be blocked!",
				HttpStatus.BAD_REQUEST);
	}

	@PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
	@DeleteMapping("/deleteuser")
	@ApiOperation(value = "Delete User")
	public ResponseEntity<String> deleteuser(@Valid @RequestParam String username){
		if(userRepository.existsByUsername("+" + username.trim())){
			Optional<User> user = userRepository.findByUsername("+" + username.trim());
			userRepository.delete(user.get());
			return ResponseEntity.ok().body("User deleted successfully!");
		}
		return new ResponseEntity<String>("Fail -> User could not be deleted!",
				HttpStatus.BAD_REQUEST);
	}

	@PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
	@GetMapping("/getuserbyusername")
	@ApiOperation(value = "Get User")
	public ResponseEntity<Optional<User>> getUserByUserName(@Valid @RequestBody GetUserByUserNameForm getUserByUserNameForm){
		if(userRepository.existsByUsername(getUserByUserNameForm.getUsername())){
			Optional<User> user = userRepository.findByUsername(getUserByUserNameForm.getUsername());
			return ResponseEntity.ok().body(user);
		}
		return new ResponseEntity<Optional<User>>(
				HttpStatus.BAD_REQUEST);
	}

	@PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
	@GetMapping("/getAllUsers")
	@ApiOperation(value = "Get All User")
	public ResponseEntity<List<User>> getAllUsers(){
		try {
			List<User> users = userRepository.findAllByUsernameIsNotNull();
			return ResponseEntity.ok().body(users);
		} catch (Exception e) {
			return new ResponseEntity<List<User>>(
					HttpStatus.BAD_REQUEST);
		}
	}

	public static String generateRandomPassword(int len) {
		// ASCII range - alphanumeric (0-9, a-z, A-Z)
		final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder();

		// each iteration of loop choose a character randomly from the given ASCII range
		// and append it to StringBuilder instance

		for (int i = 0; i < len; i++) {
			int randomIndex = random.nextInt(chars.length());
			sb.append(chars.charAt(randomIndex));
		}

		return sb.toString();
	}
}