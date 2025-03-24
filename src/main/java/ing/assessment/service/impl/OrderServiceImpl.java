package ing.assessment.service.impl;

import ing.assessment.db.order.Order;
import ing.assessment.db.order.OrderProduct;
import ing.assessment.db.product.Product;
import ing.assessment.db.repository.OrderRepository;
import ing.assessment.exception.InvalidOrder;
import ing.assessment.exception.OutOfStock;
import ing.assessment.exception.ProductNotFound;
import ing.assessment.model.Location;
import ing.assessment.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    @Autowired
    private final ProductServiceImpl productServiceImpl;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order placeOrder(List<OrderProduct> orderProducts) {
        if(orderProducts == null || orderProducts.isEmpty()) {
            throw new InvalidOrder("Invalid Order - no products specified");
        }

        Order order = Order.builder()
                .timestamp(new Date())
                .orderProducts(orderProducts)
                .build();

        double costOfOrder = calculateCostOrder(orderProducts);
        order.setOrderCost(costOfOrder);
        if(costOfOrder > 1000) {
            order.setOrderCost(costOfOrder - costOfOrder * 0.1);
            order.setDeliveryCost(0);
        } else if(costOfOrder > 500) {
            order.setDeliveryCost(0);
        }
        order.setDeliveryTime(calculateDeliveryTime(orderProducts));
        return orderRepository.save(order);
    }

    double calculateCostOrder(List<OrderProduct> orderProducts) {
        double cost = 0;
        for(OrderProduct orderProduct : orderProducts) {
            List<Product> productList = productServiceImpl.getProductsById(orderProduct.getProductId());
            boolean found = false;
            for (Product product : productList) {
                if (product.getProductCk().getLocation().equals(orderProduct.getLocation())) {
                    if(checkStock(orderProduct, product)) {
                        cost += product.getPrice() * orderProduct.getQuantity();
                        productServiceImpl.updateStock(product.getProductCk(),
                                product.getQuantity() - orderProduct.getQuantity());
                    }
                    found = true;
                }
            }
            if(!found) {
                throw new ProductNotFound("Product with id: " + orderProduct.getProductId() +
                        " not found in location " + orderProduct.getLocation());
            }
        }

        return cost;
    }

    int calculateDeliveryTime(List<OrderProduct> orderProducts) {
        int deliveryTime = 0;
        HashSet<Location> locations = new HashSet<>();
        for(OrderProduct orderProduct : orderProducts) {
            if(!locations.contains(orderProduct.getLocation())) {
                deliveryTime += 2;
                locations.add(orderProduct.getLocation());
            }
        }
        return deliveryTime;
    }

    boolean checkStock(OrderProduct orderProduct, Product product) {
        if(product.getQuantity() == 0) {
            throw new OutOfStock("Product with id: " + orderProduct.getProductId() + " from: " +
                    orderProduct.getLocation() + " is out of stock.");
        } else if(orderProduct.getQuantity() > product.getQuantity()) {
            throw new OutOfStock("Product with id: " + orderProduct.getProductId() + " from: " +
                    orderProduct.getLocation() + " is low on stock. Only " + product.getQuantity() +
                    " items left.");
        }
        return true;
    }
}