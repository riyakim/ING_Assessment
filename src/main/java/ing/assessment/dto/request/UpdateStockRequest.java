package ing.assessment.dto.request;

import ing.assessment.db.product.ProductCK;
import lombok.Data;

@Data
public class UpdateStockRequest {
    private ProductCK productCK;
    private int stock;

}
