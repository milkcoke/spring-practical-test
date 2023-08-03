package kioskapp.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StockTest {

    @Test
    @DisplayName("재고가 부족한 경우 예외 발생")
    void deductQuantity() {
        // given
        var stock = new Stock(100);
        // when & then
        assertThatThrownBy(()->stock.deductQuantity(101))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족합니다.");
    }
}
