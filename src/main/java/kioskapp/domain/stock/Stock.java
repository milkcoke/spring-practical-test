package kioskapp.domain.stock;

import jakarta.persistence.*;
import kioskapp.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Stock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity = 0;

    public Stock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("재고는 0개 이상이어야 합니다.");
        }
        this.quantity = quantity;
    }


    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void deductQuantity(int quantity) {
        if (!this.hasEnoughQuantity(quantity)) throw new IllegalArgumentException("재고가 부족합니다.");
        this.quantity -= quantity;
    }

    public boolean hasEnoughQuantity(int quantity) {
        return this.quantity >= quantity;
    }
}
