package kioskapp.domain.product;

import jakarta.persistence.*;
import kioskapp.domain.BaseEntity;
import kioskapp.domain.stock.Stock;
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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "stock_id", referencedColumnName = "id")
    private Stock stock;

    @Builder
    private Product(String name, int price, String productNumber, ProductType type, ProductSellingStatus sellingStatus, Stock stock) {
        this.name = name;
        this.price = price;
        this.productNumber = productNumber;
        this.type = type;
        this.sellingStatus = sellingStatus;
        if (ProductType.hasStock(type)) {
            this.stock = stock;
        }
    }
}