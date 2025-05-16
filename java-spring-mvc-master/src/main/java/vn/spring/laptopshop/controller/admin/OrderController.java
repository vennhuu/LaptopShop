package vn.spring.laptopshop.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.spring.laptopshop.domain.Order;
import vn.spring.laptopshop.service.OrderService;

@Controller  
public class OrderController {

  private final OrderService orderService;


  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping("/admin/order")
  public String getDashboard(Model model) {
    List<Order> orders = this.orderService.getAllOrders(); 
    model.addAttribute("orders", orders); 
    return "admin/order/show"; 
  }


  @GetMapping("/admin/order/{id}")
  public String getOrderDetailPage(Model model, @PathVariable long id) {
    Order order = this.orderService.getOrderById(id).get(); 
    model.addAttribute("order", order);
    model.addAttribute("id", id); 
    model.addAttribute("orderDetails", order.getOrderDetails()); 
    return "admin/order/detail"; 
  }


  @RequestMapping("/admin/order/update/{id}")
  public String getUpdateOrderStatusPage(Model model, @PathVariable long id) {
    Optional<Order> currentOrder = this.orderService.getOrderById(id);
    model.addAttribute("newOrder", currentOrder.get());
    return "admin/order/update";
  }

  @PostMapping("/admin/order/update")
  public String hanldeUpdateOrder(@ModelAttribute("newOrder") Order order) {
    this.orderService.updateOrder(order);
    return "redirect:/admin/order"; 
  }


  @GetMapping("/admin/order/delete/{id}")
  public String getDeleteOrderPage(Model model, @PathVariable long id) {
    model.addAttribute("id", id);
    model.addAttribute("newOrder", new Order()); 
    return "admin/order/delete"; 
  }

  // Xử lý form xóa đơn hàng
  @PostMapping("/admin/order/delete")
  public String postDeleteOrder(@ModelAttribute("newOrder") Order order) {
    this.orderService.deleteOrderById(order.getId()); 
    return "redirect:/admin/order"; 
  }
}
