package ing.assessment.service.impl;

import ing.assessment.db.product.Product;
import ing.assessment.db.product.ProductCK;
import ing.assessment.db.repository.ProductRepository;
import ing.assessment.exception.ProductNotFound;
import ing.assessment.service.ProductService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsById(Integer id) {
        List<Product> productList = productRepository.findByProductCk_Id(id);
        if(productList == null || productList.isEmpty()) {
            throw new ProductNotFound("No product found with id: " + id);
        }
        return productList;
    }

    @Override
    public Product updateStock(ProductCK productCK, Integer stock) {
        List<Product> products = getProductsById(productCK.getId());
        for(Product product : products) {
            if(product.getProductCk().getLocation().equals(productCK.getLocation())) {
                product.setQuantity(stock);
                return productRepository.save(product);
            }
        }
        throw new ProductNotFound("Product with id: " + productCK.getId() +" not found.");
    }
}