package com.devsuperior.dscatlog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatlog.dto.RoleDTO;
import com.devsuperior.dscatlog.dto.UserDTO;
import com.devsuperior.dscatlog.dto.UserInsertDTO;
import com.devsuperior.dscatlog.entities.User;
import com.devsuperior.dscatlog.repositories.RoleRepository;
import com.devsuperior.dscatlog.repositories.UserRepository;
import com.devsuperior.dscatlog.services.exceptions.DatabaseException;
import com.devsuperior.dscatlog.services.exceptions.ResourceNotFoundException;

@Service
public class UserService implements UserDetailsService {
	
	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Transactional(readOnly = true)
	public Page<UserDTO> findAll(Pageable pageable) {
		Page<User> page = userRepository.findAll(pageable);
		return page.map(user -> new UserDTO(user));
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = userRepository.findById(id);
		var entity = obj.orElseThrow(() -> new ResourceNotFoundException("Id {" + id + "} not found."));
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO update(Long id, UserDTO user) {
		try {
			var entity = userRepository.getOne(id);
			copyDtoToEntity(entity, user);
			entity = userRepository.save(entity);
			return new UserDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id {" + id + "} not found.");
		}
	}

	@Transactional
	public UserDTO insert(UserInsertDTO user) {
		var entity = new User();
		copyDtoToEntity(entity, user);
		entity.setPassword(passwordEncoder.encode(user.getPassword()));
		entity = userRepository.save(entity);
		return new UserDTO(entity);
	}

	public void delete(Long id) {
		try {
			userRepository.deleteById(id);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id {" + id + "} not found.");
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Database integrity violation.");
		}
	}

	private void copyDtoToEntity(User entity, UserDTO user) {
		entity.setEmail(user.getEmail());
		entity.setFirstName(user.getFirstName());
		entity.setLastName(user.getLastName());
		entity.getRoles().clear();
		for (RoleDTO dto : user.getRoles()) {
			var role = roleRepository.getOne(dto.getId());
			entity.getRoles().add(role);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		var user = userRepository.findByEmail(username);
		if (user == null) {
			logger.error("User not found: " + username);
			throw new UsernameNotFoundException("Email not found");
		}
		logger.info("User found: " + username);
		return user;
	}
}
