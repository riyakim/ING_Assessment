# Documentation
#### Model:
* OrderProduct class: added location attribute to easily find the specific product that was added to the order.

#### Services:
* OrderService: added getAllOrders and placeOrder methods
* OrderServiceImp: class that implemnetes OrderService interface
  * This class has an OrderRepository attribute and it uses the ProductServiceImpl instance.
  * getAllOrders method calls findAll method from the orderRepository to get all the orders that where added in the database.
  * placeOrder method has the main functionality for creating a new order. Based on a list of orderProducts, it gets the actual products that are going to be added to the order. It verifies if the list of orderProducts is null or empty and it throws InvalidOrder exception. It calls calculayteCostOrder method and based on the costOfOrder, a discound is applied (when costOfOrder > 1000), and the delivery cost is calculated as well. DeliveryTime is set based on the calculations done in calculateDeliveryTime method.
  * calculateCostOrder method calcualtes the cost of order as a sum of the cost of each product multiplied by the quantity from order product. It verifies if the product can be added to the order by checking the remaning stock. Also, after the calculation of cost, teh method updates the quantity of each product from the order, so that when another order is being placed, the stock will be correct. It throws ProductNotFound exception if the product is not found in the specified location from teh orderProduct.
  * calculateDeliveryTime method calcualtes the delivery time based on the location of products (for every different location the deliveryTime is added by 2)
  * checkStock method checks if the quantity of the actual product is enough for buying the needed quantity soecified in teh order product. Otherwise, OutOfStock exception is thrown.
* ProductServiceImpl: added updateStock method to modify the available quantity of a product.

### Controller
* OrderController: it has orderService attribute
  * getAllOrders method returs a list with the orders found and Httpstatus = ok and it returns Httpstatus 204 (NO_CONTENT) if any order could not be found
  * placeOrder method returns teh order that was created and HttpStatus 201(CREATED)
* ProductController:
  * getProduct method: throws ProductNotFound exception
 
### DTO
* UpdateStockRequest: contains productCk and stock and it is the Request body for the update Stock put request
* ErrorResponse: contains message that is shown when getting an exception

#### Exceptions
* InvalidOrder: exception to be triggered when an order has list of orderProducts null or empty.
* OutOfStock: exception to be triggered when an order cannot be created due to lack of needed items.
* ProductNotFound: exception to be triggered when a product from the orderProduct list cannot be found by id.
* GlobalExceptionHandler: contains handler for each exception mentioned above. Each methid return a ResponseEntity with an error message and a specific HttpStatus.

### Unit test
* Added spring-boot-starter-test dependency in pom.xml
* Added @Builder adnotations to Order, Product, OrderProduct, ProductCk for creation of objects for the test cases.
* OrderServiceImplTest class contains test cases for all the methods from OrderServiceImpl class.
* getAllOrders_ListOfOrders_CanFindOrders = test for getAllOrders method
* placeOrder_CreateOrder_ValidInput = test for placeOrder method where the input is valid and an order can be created successfully.
* placeOrder_DiscountOrderCost_CostOfOrderGraterThan1000 = test for placeOrder method where the cost of order is bigger than 1000. In this case a 10% discount is applied to the order cost.
* placeOrder_0DeliveryCost_CostOfOrderGraterThan1000 = test for placeOrder method where the cost of order is grated than 1000 and delivery cost is calculated to 0.
* placeOrder_0DeliveryCost_CostOfOrderGraterThan500 = test for placeOrder method where the cost or order is grated than 500 and delivery cost is 0.
* placeOrder_ThrowInvalidOrder_NullOrderProductsList = test for placeOrder methid where a Invalid Order exception is thrown because the orderProducts is null.
* placeOrder_ThrowInvalidOrder_EmptyOrderProductsList = test for palceOrder method where a Invalid Order exception is thrown because the orderProducts is empty.
* calculateCostOrder_ReturnCost_ValidOrderProducts = test for calculating the cost of order
* calculateCostOrder_ThrowProductNotFoundInLocation_NoProductInLocation = test for calculateCostOrder method when throwing ProductNotFound exception
* calculateDeliveryTime_DeliveryTime_AllProductsInSameLocation = test the calculation of delivery time when all products are in the same location.
* calculateDeliveryTime_DeliveryTime_ProductsInDifferentLocations = test the calculation of delivery time when products are in diferent locations.
* checkStock_ReturnTrue_ProductInStock = test checkStock method in valid case
* checkStock_ThrowOutOfStock_ProductInLowInStock = test checkStock when throwing OutOfStock exception when product is low in stock
* checkStock_ThrowOutOfStock_ProductIsOutOfStock =  test checkStock when throwing OutOfStock exception when product is out of stock

# Prerequisites

In order to be able to submit your assignement, you should have the following installed:
* JDK >= 17
* Maven
* Postman / any other tool that allows you to hit the application's endpoints
* Any versioning tool
* Any IDE that allows you to run the application


# How to

#### Run the application
The application should be run as a SpringBootApplication. Below is a quick guide on how to do that via IntelliJ:
* Edit Configuration 
   * Add New Configuration (Spring Boot)
     * Change the **Main class** to **ing.assessment.INGAssessment**
       * Run the app.

#### Connect to the H2 database
Access the following url: **http://localhost:8080/h2-console/**
 * **Driver Class**: _**org.h2.Driver**_
 * **JDBC URL**: _**jdbc:h2:mem:testdb**_
 * **User Name**: _**sa**_
 * **Password**: **_leave empty_**


