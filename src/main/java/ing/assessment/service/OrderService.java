package ing.assessment.service;

import ing.assessment.db.order.Order;
import ing.assessment.db.order.OrderProduct;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    void placeOrder(List<OrderProduct> orderProducts);
}