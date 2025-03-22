package ing.assessment.dto.response;

import ing.assessment.db.order.Order;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaceOrderResponse {
    private Order order;
}
