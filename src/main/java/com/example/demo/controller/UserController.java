package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserDto;
import com.example.demo.exeption.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.service.UserService;



@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping
	public UserDto createUser(@Valid @RequestBody UserDto userDto)  {
//		if(result.hasErrors()) {
//			System.out.println("has errors");
//		}
//		System.out.println("error count "+result.getErrorCount());
		
		User user = convertToEntity(userDto);
		return convertToDto(userService.createUser(user));
	}

	private UserDto convertToDto(User user) {
		UserDto dto = new UserDto();
		dto.setId(user.getId());
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		dto.setEmail(user.getEmail());
		return dto;
	}

	private User convertToEntity(UserDto dto)
			 {

		User user = new User();
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setEmail(dto.getEmail());
//		if (user.getFirstName().isEmpty()) {
//			throw new NoClassDefFoundError();
//		}

		return user;
	}

	@GetMapping
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/{id}")
	public User getUser(@PathVariable Long id) {
		Optional<User> user = userService.getUserById(id);
		return user.orElse(null);
	}

	@PutMapping("/{id}")
	public UserDto updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
		User updated = userService.updateUser(id, convertToEntity(userDto))
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
		return convertToDto(updated);
	}

	@DeleteMapping
	public void deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
	}

}
