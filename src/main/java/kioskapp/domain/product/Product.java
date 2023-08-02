package kioskapp.domain.product;

import jakarta.persistence.*;
import kioskapp.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int price;
    private String productNumber;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Enumerated(EnumType.STRING)
    private ProductSellingStatus sellingStatus;

    @Builder
    private Product(String name, int price, String productNumber, ProductType type, ProductSellingStatus sellingStatus) {
        this.name = name;
        this.price = price;
        this.productNumber = productNumber;
        this.type = type;
        this.sellingStatus = sellingStatus;
    }
}