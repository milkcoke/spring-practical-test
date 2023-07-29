package kioskapp;

import kioskapp.domain.beverage.Americano;
import kioskapp.domain.beverage.Latte;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CafeKioskTest {

    @Test
    @DisplayName("success - 아메리카노 1잔 추가시 주문 목록에 담긴다.")
    void add() {
        var cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        assertThat(cafeKiosk.getBeverages()).hasSize(1);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("Americano");
    }

    @Test
    @DisplayName("success - 아메리카노 2잔 추가시 주문 목록에 2잔 담긴다.")
    void addSeveralBeverages() {
        // Arrange
        var cafeKiosk = new CafeKiosk();

        // Act
        cafeKiosk.add(new Americano(), 2);

        //  Assert
        assertThat(cafeKiosk.getBeverages()).hasSize(2);
    }

    @Test
    @DisplayName("fail - 아메리카노 0잔 이하 추가 주문 불가능")
    void failedToAddServeralBeverages() {
        var cafeKiosk = new CafeKiosk();
        assertThatThrownBy(() -> cafeKiosk.add(new Americano(), 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("You should order beverage greater than or equal to 1");
    }

    @Test
    @DisplayName("success - 아메리카노 한잔 담았다가 취소하면 목록에서 제거된다.")
    void remove() {
        // Arrange
        var cafeKiosk = new CafeKiosk();
        var americano = new Americano();

        // Act
        cafeKiosk.add(americano);
        // Assert
        assertThat(cafeKiosk.getBeverages()).hasSize(1);

        // Act
        cafeKiosk.remove(americano);
        // Assert
        assertThat(cafeKiosk.getBeverages()).hasSize(0);
    }

    @Test
    @DisplayName("success - 모든 음료 취소시 주문 목록이 0잔이 된다.")
    void clear() {
        // Arrange
        var cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());
        cafeKiosk.add(new Latte());

        // Act & Assert
        assertThat(cafeKiosk.getBeverages()).hasSize(2);

        // Act
        cafeKiosk.clear();
        // Assert
        assertThat(cafeKiosk.getBeverages()).hasSize(0);
    }

    @Test
    @DisplayName("success - 영업 시작 시간 07시 주문 가능")
    void createOrderWithOpenTime() {
        // Arrange
        var cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());
        cafeKiosk.add(new Latte());
        var testDateTime = LocalDateTime.of(2023,07,23,07,0,0);

        // Act
        var order = cafeKiosk.createOrder(testDateTime);

        // Assert
        assertThat(order.getBeverages()).hasSize(2);
    }

    @Test
    @DisplayName("fail - 영업 시작 시간 7시 이전 주문 불가")
    void createOrderWithBeforeOpenTime() {
        // Arrange
        var cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());
        cafeKiosk.add(new Latte());
        var testDateTime = LocalDateTime.of(2023,07,23,06,59,0);

        // Act & Assert
        assertThatThrownBy(() -> cafeKiosk.createOrder(testDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("You cannot place an order between 07:00 and 18:00");
    }

    @Test
    @DisplayName("success - 아메리카노 2잔 및 라떼 2잔 총 가격 17,000원")
    void calculateTotalPrice() {
        // Arrange
        var cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano(), 2);
        cafeKiosk.add(new Latte(), 2);

        // Act
        var totalPrice = cafeKiosk.calculateTotalPrice();

        // Assert
        assertThat(totalPrice).isEqualTo(17_000);
    }
}