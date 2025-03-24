package ing.assessment.db.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @EmbeddedId
    private ProductCK productCk;

    private String name;
    private Double price;
    private Integer quantity;
}