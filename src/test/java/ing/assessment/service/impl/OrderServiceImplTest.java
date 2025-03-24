package ing.assessment.service.impl;

import ing.assessment.db.order.Order;
import ing.assessment.db.order.OrderProduct;
import ing.assessment.db.product.Product;
import ing.assessment.db.product.ProductCK;
import ing.assessment.db.repository.OrderRepository;
import ing.assessment.db.repository.ProductRepository;
import ing.assessment.exception.InvalidOrder;
import ing.assessment.exception.OutOfStock;
import ing.assessment.exception.ProductNotFound;
import ing.assessment.model.Location;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductServiceImpl productServiceImpl;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productServiceImpl = new ProductServiceImpl(productRepository);
        orderService = new OrderServiceImpl(orderRepository, productServiceImpl);
    }

    @Test
    public void getAllOrders_ListOfOrders_CanFindOrders() {
        List<Order> orders = new ArrayList<>();

        when(orderRepository.findAll()).thenReturn(orders);
        Assertions.assertNotNull(orderService.getAllOrders());
    }

    @Test
    public void placeOrder_CreateOrder_ValidInput() {
        List<OrderProduct> orderProducts = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        ProductCK productCK = ProductCK.builder()
                .id(1)
                .location(Location.MUNICH)
                .build();

        Product product = Product.builder()
                .productCk(productCK)
                .name("Shoes")
                .price(50.0)
                .quantity(400)
                .build();
        products.add(product);

        OrderProduct orderProduct = OrderProduct.builder()
                .productId(1)
                .location(Location.MUNICH)
                .quantity(2)
                .build();
        orderProducts.add(orderProduct);

        Order order = Order.builder()
                .orderProducts(orderProducts)
                .orderCost(100.0)
                .deliveryCost(null)
                .deliveryTime(2)
                .timestamp(new Date())
                .build();

        when(orderRepository.save(Mockito.any(Order.class)))
                .then(invocation -> {
                    return invocation.getArgument(0);
                });
        when(productRepository.findByProductCk_Id(1)).thenReturn(products);

        Order resultOrder = orderService.placeOrder(orderProducts);

        Assertions.assertEquals(order.getOrderCost(), resultOrder.getOrderCost());
        Assertions.assertEquals(order.getOrderProducts(), resultOrder.getOrderProducts());
        Assertions.assertEquals(order.getId(), resultOrder.getId());
        Assertions.assertEquals(order.getDeliveryCost(), resultOrder.getDeliveryCost());
        Assertions.assertEquals(order.getDeliveryCost(), resultOrder.getDeliveryCost());

    }

    @Test
    public void placeOrder_DiscountOrderCost_CostOfOrderGraterThan1000() {
        List<OrderProduct> orderProducts = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        ProductCK productCK = ProductCK.builder()
                .id(1)
                .location(Location.MUNICH)
                .build();

        Product product = Product.builder()
                .productCk(productCK)
                .name("Shoes")
                .price(50.0)
                .quantity(400)
                .build();
        products.add(product);

        OrderProduct orderProduct = OrderProduct.builder()
                .productId(1)
                .location(Location.MUNICH)
                .quantity(10)
                .build();
        orderProducts.add(orderProduct);

        ProductCK productCK2 = ProductCK.builder()
                .id(1)
                .location(Location.COLOGNE)
                .build();

        Product product2 = Product.builder()
                .productCk(productCK2)
                .name("Shirt")
                .price(50.0)
                .quantity(400)
                .build();
        products.add(product2);

        OrderProduct orderProduct2 = OrderProduct.builder()
                .productId(1)
                .location(Location.COLOGNE)
                .quantity(12)
                .build();
        orderProducts.add(orderProduct2);

        when(orderRepository.save(Mockito.any(Order.class)))
                .then(invocation -> {
                    return invocation.getArgument(0);
                });
        when(productRepository.findByProductCk_Id(1)).thenReturn(products);

        Order resultOrder = orderService.placeOrder(orderProducts);

        Assertions.assertEquals(990, resultOrder.getOrderCost());
    }

    @Test
    public void placeOrder_0DeliveryCost_CostOfOrderGraterThan1000() {
        List<OrderProduct> orderProducts = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        ProductCK productCK = ProductCK.builder()
                .id(1)
                .location(Location.MUNICH)
                .build();

        Product product = Product.builder()
                .productCk(productCK)
                .name("Shoes")
                .price(50.0)
                .quantity(400)
                .build();
        products.add(product);

        OrderProduct orderProduct = OrderProduct.builder()
                .productId(1)
                .location(Location.MUNICH)
                .quantity(10)
                .build();
        orderProducts.add(orderProduct);

        ProductCK productCK2 = ProductCK.builder()
                .id(1)
                .location(Location.COLOGNE)
                .build();

        Product product2 = Product.builder()
                .productCk(productCK2)
                .name("Shirt")
                .price(50.0)
                .quantity(400)
                .build();
        products.add(product2);

        OrderProduct orderProduct2 = OrderProduct.builder()
                .productId(1)
                .location(Location.COLOGNE)
                .quantity(12)
                .build();
        orderProducts.add(orderProduct2);

        when(orderRepository.save(Mockito.any(Order.class)))
                .then(invocation -> {
                    return invocation.getArgument(0);
                });
        when(productRepository.findByProductCk_Id(1)).thenReturn(products);

        Order resultOrder = orderService.placeOrder(orderProducts);

        Assertions.assertEquals(0, resultOrder.getDeliveryCost());
    }

    @Test
    public void placeOrder_0DeliveryCost_CostOfOrderGraterThan500() {
        List<OrderProduct> orderProducts = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        ProductCK productCK = ProductCK.builder()
                .id(1)
                .location(Location.MUNICH)
                .build();

        Product product = Product.builder()
                .productCk(productCK)
                .name("Shoes")
                .price(50.0)
                .quantity(400)
                .build();
        products.add(product);

        OrderProduct orderProduct = OrderProduct.builder()
                .productId(1)
                .location(Location.MUNICH)
                .quantity(10)
                .build();
        orderProducts.add(orderProduct);

        ProductCK productCK2 = ProductCK.builder()
                .id(1)
                .location(Location.COLOGNE)
                .build();

        Product product2 = Product.builder()
                .productCk(productCK2)
                .name("Shirt")
                .price(50.0)
                .quantity(400)
                .build();
        products.add(product2);

        OrderProduct orderProduct2 = OrderProduct.builder()
                .productId(1)
                .location(Location.COLOGNE)
                .quantity(5)
                .build();
        orderProducts.add(orderProduct2);


        when(orderRepository.save(Mockito.any(Order.class)))
                .then(invocation -> {
                    return invocation.getArgument(0);
                });
        when(productRepository.findByProductCk_Id(1)).thenReturn(products);

        Order resultOrder = orderService.placeOrder(orderProducts);

        Assertions.assertEquals(0, resultOrder.getDeliveryCost());
    }
    @Test
    public void placeOrder_ThrowInvalidOrder_NullOrderProductsList() {
        Exception exception = Assertions.assertThrows(InvalidOrder.class, () -> {
            orderService.placeOrder(null);
        });

        Assertions.assertEquals("Invalid Order - no products specified", exception.getMessage());
    }

    @Test
    public void placeOrder_ThrowInvalidOrder_EmptyOrderProductsList() {
        Exception exception = Assertions.assertThrows(InvalidOrder.class, () -> {
            orderService.placeOrder(new ArrayList<>());
        });

        Assertions.assertEquals("Invalid Order - no products specified", exception.getMessage());
    }

    @Test
    public void calculateCostOrder_ReturnCost_ValidOrderProducts() {
        List<OrderProduct> orderProducts = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        ProductCK productCK = ProductCK.builder()
                .id(1)
                .location(Location.MUNICH)
                .build();

        Product product = Product.builder()
                .productCk(productCK)
                .name("Shoes")
                .price(50.0)
                .quantity(400)
                .build();
        products.add(product);

        OrderProduct orderProduct = OrderProduct.builder()
                .productId(1)
                .location(Location.MUNICH)
                .quantity(10)
                .build();
        orderProducts.add(orderProduct);

        ProductCK productCK2 = ProductCK.builder()
                .id(1)
                .location(Location.COLOGNE)
                .build();

        Product product2 = Product.builder()
                .productCk(productCK2)
                .name("Shirt")
                .price(50.0)
                .quantity(400)
                .build();
        products.add(product2);

        OrderProduct orderProduct2 = OrderProduct.builder()
                .productId(1)
                .location(Location.COLOGNE)
                .quantity(5)
                .build();
        orderProducts.add(orderProduct2);

        when(productRepository.findByProductCk_Id(1)).thenReturn(products);

        Assertions.assertEquals(750, orderService.calculateCostOrder(orderProducts));
    }

    @Test
    public void calculateCostOrder_ThrowProductNotFoundInLocation_NoProductInLocation() {
        List<OrderProduct> orderProducts = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        ProductCK productCK = ProductCK.builder()
                .id(1)
                .location(Location.MUNICH)
                .build();

        Product product = Product.builder()
                .productCk(productCK)
                .name("Shoes")
                .price(50.0)
                .quantity(400)
                .build();
        products.add(product);

        OrderProduct orderProduct = OrderProduct.builder()
                .productId(1)
                .location(Location.COLOGNE)
                .quantity(10)
                .build();
        orderProducts.add(orderProduct);

        when(productRepository.findByProductCk_Id(1)).thenReturn(products);

        Exception exception = Assertions.assertThrows(ProductNotFound.class, () -> {
            orderService.calculateCostOrder(orderProducts);
        });

        Assertions.assertEquals("Product with id: 1 not found in location COLOGNE", exception.getMessage());
    }
    @Test
    public void calculateDeliveryTime_DeliveryTime_AllProductsInSameLocation() {
        List<OrderProduct> orderProducts = new ArrayList<>();

        OrderProduct orderProduct = OrderProduct.builder()
                .productId(1)
                .location(Location.MUNICH)
                .quantity(10)
                .build();
        orderProducts.add(orderProduct);

        OrderProduct orderProduct2 = OrderProduct.builder()
                .productId(2)
                .location(Location.MUNICH)
                .quantity(5)
                .build();
        orderProducts.add(orderProduct2);

        Assertions.assertEquals(2, orderService.calculateDeliveryTime(orderProducts));

    }

    @Test
    public void calculateDeliveryTime_DeliveryTime_ProductsInDifferentLocations() {
        List<OrderProduct> orderProducts = new ArrayList<>();

        OrderProduct orderProduct = OrderProduct.builder()
                .productId(1)
                .location(Location.MUNICH)
                .quantity(10)
                .build();
        orderProducts.add(orderProduct);

        OrderProduct orderProduct2 = OrderProduct.builder()
                .productId(1)
                .location(Location.COLOGNE)
                .quantity(5)
                .build();
        orderProducts.add(orderProduct2);

        Assertions.assertEquals(4, orderService.calculateDeliveryTime(orderProducts));
    }

    @Test
    public void checkStock_ReturnTrue_ProductInStock() {

        ProductCK productCK = ProductCK.builder()
                .id(1)
                .location(Location.MUNICH)
                .build();

        Product product = Product.builder()
                .productCk(productCK)
                .name("Shoes")
                .price(50.0)
                .quantity(400)
                .build();

        OrderProduct orderProduct = OrderProduct.builder()
                .productId(1)
                .location(Location.MUNICH)
                .quantity(10)
                .build();

        Assertions.assertTrue(orderService.checkStock(orderProduct, product));
    }

    @Test
    public void checkStock_ThrowOutOfStock_ProductInLowInStock() {

        ProductCK productCK = ProductCK.builder()
                .id(1)
                .location(Location.MUNICH)
                .build();

        Product product = Product.builder()
                .productCk(productCK)
                .name("Shoes")
                .price(50.0)
                .quantity(40)
                .build();

        OrderProduct orderProduct = OrderProduct.builder()
                .productId(1)
                .location(Location.MUNICH)
                .quantity(50)
                .build();

        Exception exception = Assertions.assertThrows(OutOfStock.class, () -> {
            orderService.checkStock(orderProduct, product);
        });

        Assertions.assertEquals("Product with id: 1 from: MUNICH is low on stock. Only 40 items left.",
                exception.getMessage());
    }

    @Test
    public void checkStock_ThrowOutOfStock_ProductIsOutOfStock() {

        ProductCK productCK = ProductCK.builder()
                .id(1)
                .location(Location.MUNICH)
                .build();

        Product product = Product.builder()
                .productCk(productCK)
                .name("Shoes")
                .price(50.0)
                .quantity(0)
                .build();

        OrderProduct orderProduct = OrderProduct.builder()
                .productId(1)
                .location(Location.MUNICH)
                .quantity(50)
                .build();

        Exception exception = Assertions.assertThrows(OutOfStock.class, () -> {
            orderService.checkStock(orderProduct, product);
        });

        Assertions.assertEquals("Product with id: 1 from: MUNICH is out of stock.", exception.getMessage());
    }
}
