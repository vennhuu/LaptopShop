package vn.spring.laptopshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import vn.spring.laptopshop.domain.Cart;
import vn.spring.laptopshop.domain.CartDetail;
import vn.spring.laptopshop.domain.Order;
import vn.spring.laptopshop.domain.OrderDetail;
import vn.spring.laptopshop.domain.Product;
import vn.spring.laptopshop.domain.User;
import vn.spring.laptopshop.repository.CartDetailRepository;
import vn.spring.laptopshop.repository.CartRepository;
import vn.spring.laptopshop.repository.OrderDetailRepository;
import vn.spring.laptopshop.repository.OrderRepository;
import vn.spring.laptopshop.repository.ProductRepository;

@Service
public class ProductService {
  private final ProductRepository productRepository;
  private final CartRepository cartRepository;
  private final CartDetailRepository cartDetailRepository;
  private final UserService userService;
  private final OrderRepository orderRepository;
  private final OrderDetailRepository orderDetailRepository;

  public ProductService(ProductRepository productRepository,
      CartRepository cartRepository,
      CartDetailRepository cartDetailRepository,
      UserService userService,
      OrderRepository orderRepository,
      OrderDetailRepository orderDetailRepository) {
    this.productRepository = productRepository;
    this.cartRepository = cartRepository;
    this.cartDetailRepository = cartDetailRepository;
    this.userService = userService;
    this.orderRepository = orderRepository;
    this.orderDetailRepository = orderDetailRepository;
  }

  public List<Product> getAllProducts() {
    return this.productRepository.findAll();
  }

  public Optional<Product> getProductById(long id) {
    return this.productRepository.findById(id);
  }

  public Product createProduct(Product product) {
    return this.productRepository.save(product);
  }

  public void deleteProduct(long id) {
    this.productRepository.deleteById(id);
  }

  public void handleAddProductToCart(String email, long productId, HttpSession session) {
    User user = this.userService.getUserByEmail(email);
    if (user != null) {
      Cart cart = this.cartRepository.findByUser(user);
      if (cart == null) {
        // tạo mới cart
        Cart otherCart = new Cart();
        otherCart.setUser(user);
        otherCart.setSum(0);

        cart = this.cartRepository.save(otherCart);
      }

      // save cart detail
      // tìm product by ID

      Optional<Product> productOptional = this.productRepository.findById(productId);
      if (productOptional.isPresent()) {
        Product realProduct = productOptional.get();
        // check sản phẩm đã có trong giỏ hàng trước đây chưa?
        CartDetail oldDetail = this.cartDetailRepository.findByCartAndProduct(cart, realProduct);

        if (oldDetail == null) {
          CartDetail cartDetail = new CartDetail();
          cartDetail.setCart(cart);
          cartDetail.setProduct(realProduct);
          cartDetail.setPrice(realProduct.getPrice());
          cartDetail.setQuantity(1);
          this.cartDetailRepository.save(cartDetail);

          // update cart sum
          int s = cart.getSum() + 1;
          cart.setSum(cart.getSum() + 1);
          cart = this.cartRepository.save(cart);
          session.setAttribute("sum", s);
        } else {
          oldDetail.setQuantity(oldDetail.getQuantity() + 1);
          this.cartDetailRepository.save(oldDetail);
        }
      }
    }
  }

  public Cart fetchByUser(User user) {
    return this.cartRepository.findByUser(user);
  }

  public void handleRemoveCartDetail(long cartDetailId, HttpSession session) {
    // tìm product by ID
    Optional<CartDetail> cartDetailOptional = this.cartDetailRepository.findById(cartDetailId);
    if (cartDetailOptional.isPresent()) {
      CartDetail cartDetail = cartDetailOptional.get();

      Cart currentCart = cartDetail.getCart();

      // delete cart detail
      this.cartDetailRepository.deleteById(cartDetailId);

      // update cart

      if (currentCart.getSum() > 1) {
        // update current cart sum
        int s = currentCart.getSum() - 1;
        currentCart.setSum(s);
        session.setAttribute("sum", s);
        this.cartRepository.save(currentCart);
      } else {
        // delete cart (sum = 1)
        this.cartRepository.deleteById(currentCart.getId());
        session.setAttribute("sum", 0);
      }
    }
  }

  public void handleUpdateCartBeforeCheckout(List<CartDetail> cartDetails) {
    for (CartDetail cartDetail : cartDetails) {
      Optional<CartDetail> cdOptional = this.cartDetailRepository.findById(cartDetail.getId());
      if (cdOptional.isPresent()) {
        CartDetail currentCartDetail = cdOptional.get();
        currentCartDetail.setQuantity(cartDetail.getQuantity());
        this.cartDetailRepository.save(currentCartDetail);
      }
    }
  }

  public void handlePlaceOrder(
      User user, HttpSession session,
      String receiverName, String receiverAddress, String receiverPhone) {

    // step 1: get cart by user
    Cart cart = this.cartRepository.findByUser(user);
    if (cart != null) {
      List<CartDetail> cartDetails = cart.getCartDetails();
      if (cartDetails != null) {

        // create order
        Order order = new Order();
        order.setUser(user);
        order.setReceiverName(receiverName);
        order.setReceiverAddress(receiverAddress);
        order.setReceiverPhone(receiverPhone);
        order.setStatus("PENDING");

        double sum = 0;
        for (CartDetail cd : cartDetails) {
          sum += cd.getPrice();
        }
        order.setTotalPrice(sum);
        order = this.orderRepository.save(order);

        // create orderDetail

        for (CartDetail cd : cartDetails) {
          OrderDetail orderDetail = new OrderDetail();
          orderDetail.setOrder(order);
          orderDetail.setProduct(cd.getProduct());
          orderDetail.setPrice(cd.getPrice());
          orderDetail.setQuantity(cd.getQuantity());

          this.orderDetailRepository.save(orderDetail);
        }

        // step 2: delete cart_detail and cart
        for (CartDetail cd : cartDetails) {
          this.cartDetailRepository.deleteById(cd.getId());
        }

        this.cartRepository.deleteById(cart.getId());

        // step 3: update session
        session.setAttribute("sum", 0);
      }

    }

  }

}