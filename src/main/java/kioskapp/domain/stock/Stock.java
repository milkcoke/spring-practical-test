package kioskapp.domain.stock;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kioskapp.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** stock vs product 1:1 Mapping
 * product 없이 재고는 있을 수 없다.
 * stock 이 FK productId 갖는게 데이터 모델링상은 맞음
 * 그러나 자주 참조하는 것은 product 에서 재고 상황임
 * 그러므로 개발 편의상 product 가 재고를 갖게 설계하자.
 * stock 입장에서 product 를 알 필요가 없음.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Stock extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity = 0;

    public Stock(int quantity) {
        this.quantity = quantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void deductQuantity(int quantity) {
        if (this.quantity - quantity < 0) throw new IllegalArgumentException("재고가 부족합니다.");
        this.quantity -= quantity;
    }
}
