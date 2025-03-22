package ing.assessment.controller;

import ing.assessment.db.product.Product;
import ing.assessment.dto.request.UpdateStockRequest;
import ing.assessment.dto.response.UpdateStockResponse;
import ing.assessment.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Product>> getProduct(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductsById(id));
    }

    @PutMapping
    public ResponseEntity<UpdateStockResponse> updateStock(@RequestBody UpdateStockRequest request) {
        return ResponseEntity.status(HttpStatus.OK).
                body(UpdateStockResponse.builder().
                        product(productService.updateStock(request.getProductCK(), request.getStock())).build());
    }
}