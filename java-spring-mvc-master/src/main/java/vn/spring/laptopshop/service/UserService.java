package vn.spring.laptopshop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.spring.laptopshop.domain.Role;
import vn.spring.laptopshop.domain.User;
import vn.spring.laptopshop.domain.dto.RegisterDTO;
import vn.spring.laptopshop.repository.OrderRepository;
import vn.spring.laptopshop.repository.ProductRepository;
import vn.spring.laptopshop.repository.RoleRepository;
import vn.spring.laptopshop.repository.UserRepository;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;

  public UserService(UserRepository userRepository,
      RoleRepository roleRepository,
      OrderRepository orderRepository,
      ProductRepository productRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.orderRepository = orderRepository;
    this.productRepository = productRepository;
  }

  public List<User> getAllUsers() {
    return this.userRepository.findAll();
  }

  public Page<User> fetchUsers(Pageable page) {
    return this.userRepository.findAll(page) ;
  }
  public List<User> getOneUserByEmail(String email) {
    return this.userRepository.findOneByEmail(email);
  }

  public User getUserById(long id) {
    return this.userRepository.findById(id);
  }

  public User handleSaveUser(User user) {
    User eric = this.userRepository.save(user);
    System.out.println(eric);
    return eric;
  }

  public void deleteUser(long id) {
    this.userRepository.deleteById(id);
  }

  public Role getRoleByName(String name) {
    return this.roleRepository.findByName(name);
  }

  public User registerDTOtoUser(RegisterDTO registerDTO) {
    User user = new User();
    user.setFullName(registerDTO.getFirstName() + " " + registerDTO.getLastName());
    user.setEmail(registerDTO.getEmail());
    user.setPassword(registerDTO.getPassword());
    return user;
  }

  public boolean checkEmailExist(String email) {
    return this.userRepository.existsByEmail(email);
  }

  public User getUserByEmail(String email) {
    return this.userRepository.findByEmail(email);
  }

  public long countUsers() {
    return this.userRepository.count();

  }

  public long countProducts() {
    return this.productRepository.count();
  }

  public long countOrders() {
    return this.orderRepository.count();
  }

}
