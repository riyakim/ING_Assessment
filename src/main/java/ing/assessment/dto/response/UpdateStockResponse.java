package ing.assessment.dto.response;

import ing.assessment.db.product.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateStockResponse {
    private Product product;
}
