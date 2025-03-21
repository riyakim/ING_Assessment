package ing.assessment.controller;

import ing.assessment.db.product.Product;
import ing.assessment.exception.ProductNotFound;
import ing.assessment.service.ProductService;
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
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public List<Product> getProduct(@PathVariable("id") Integer id) {
        List<Product> products = productService.getProductsById(id);
        if(products == null || products.isEmpty()) {
            throw new ProductNotFound("No product found with id: " + id);
        }
        return products;
    }

    // TODO update stock
}