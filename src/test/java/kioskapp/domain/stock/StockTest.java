package kioskapp.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.*;

class StockTest {

    @Test
    @DisplayName("재고 값은 항상 0 이상만 입력 가능하다.")
    public void negativeQuantityStock() {
        assertThatThrownBy(() -> new Stock(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고는 0개 이상이어야 합니다.");
    }

    @Test
    @DisplayName("재고가 부족한 경우 재고 차감할 수 없다.")
    void deductQuantityFail() {
        // given
        var stock = new Stock(100);
        // when & then
        assertThatThrownBy(()->stock.deductQuantity(101))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족합니다.");
    }

    @Test
    @DisplayName("재고가 충분한 경우 재고 차감")
    public void deductQuantitySuccess() {
      // given
      var stock = new Stock(100);
      // when
      stock.deductQuantity(100);
      // then
      assertThat(stock.getQuantity()).isZero();
    }

    /**
     * 공유 리소스를 시작하여
     * 환경을 공유하며 단계별 시나리오 테스트하기 용이하다.
     */
    @DisplayName("재고 차감 시나리오")
    @TestFactory
    Collection<DynamicTest> deductQuantityNotEnoughStock() {
        // Shared resource
        Stock stock = new Stock(1);

        return List.of(
            dynamicTest("재고 개수만큼 수량 차감 가능", ()->{
                // given
                int quantity = 1;

                // when
                stock.deductQuantity(quantity);

                //then
                assertThat(stock.getQuantity()).isZero();
            }),

            dynamicTest("재고가 부족한 경우 재고 차감 불가능", ()->{
                // given
                int quantity = 1;

                // when // then
                assertThatThrownBy(()->stock.deductQuantity(quantity))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("재고가 부족합니다.");
            })
        );
    }
}
