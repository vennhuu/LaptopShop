package vn.spring.laptopshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.spring.laptopshop.domain.Order;
import vn.spring.laptopshop.domain.OrderDetail;
import vn.spring.laptopshop.repository.CartDetailRepository;
import vn.spring.laptopshop.repository.CartRepository;
import vn.spring.laptopshop.repository.OrderDetailRepository;
import vn.spring.laptopshop.repository.OrderRepository;
import vn.spring.laptopshop.repository.ProductRepository;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderDetailRepository orderDetailRepository;

  public OrderService(
      OrderRepository orderRepository,
      OrderDetailRepository orderDetailRepository) {
    this.orderRepository = orderRepository;
    this.orderDetailRepository = orderDetailRepository;
  }

  public List<Order> getAllOrders() {
    return this.orderRepository.findAll();
  }

  public Optional<Order> getOrderById(long id) {
    return this.orderRepository.findById(id);
  }

  public void deleteOrderById(long id) {
    // delete order detail first
    Optional<Order> orOptional = this.getOrderById(id);
    if (orOptional.isPresent()) {
      Order order = orOptional.get();
      List<OrderDetail> orderDetails = order.getOrderDetails();
      for (OrderDetail orderDetail : orderDetails) {
        this.orderDetailRepository.deleteById(orderDetail.getId());
      }
    }
    // then delete order
    this.orderRepository.deleteById(id);
  }

  public List<OrderDetail> getAllOrderDetails() {
    return this.orderDetailRepository.findAll();
  }

  public Optional<OrderDetail> getOrderDetailById(long id) {
    return this.orderDetailRepository.findById(id);
  }

  public void deleteOrderDetailById(long id) {
    this.orderDetailRepository.deleteById(id);
  }

  public void updateOrder(Order order) {
    Optional<Order> orderOptional = this.getOrderById(order.getId());
    if (orderOptional.isPresent()) {
      Order currentOrder = orderOptional.get();
      currentOrder.setStatus(order.getStatus());
      this.orderRepository.save(currentOrder);
    }
  }

}
