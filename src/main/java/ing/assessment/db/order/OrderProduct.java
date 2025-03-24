package ing.assessment.db.order;

import ing.assessment.model.Location;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Builder
public class OrderProduct {

    private Integer productId;
    private Integer quantity;
    private Location location;
}