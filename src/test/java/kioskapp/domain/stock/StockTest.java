package kioskapp.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
}
