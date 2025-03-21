package ing.assessment.dto.request;

import ing.assessment.db.product.ProductCK;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStockRequest {
    private ProductCK productCK;
    private int stock;
}
