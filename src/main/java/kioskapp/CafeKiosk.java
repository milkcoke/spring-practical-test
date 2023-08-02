package kioskapp;

import kioskapp.domain.beverage.Beverage;
import kioskapp.domain.order.CafeOrder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CafeKiosk {

    private static final LocalTime SHOP_OPEN_TIME = LocalTime.of(7, 0);
    private static final LocalTime SHOP_CLOSE_TIME = LocalTime.of(18, 0);

    private final List<Beverage> beverages = new ArrayList<>();

    public void add(Beverage beverage) {
        beverages.add(beverage);
    }

    public void add(Beverage beverage, int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("You should order beverage greater than or equal to 1");
        }
        for (var i = 0; i < count; i++) {
            beverages.add(beverage);
        }
    }

    public void remove(Beverage beverage) {
        beverages.remove(beverage);
    }

    public void clear() {
        beverages.clear();
    }

    public int calculateTotalPrice() {
        return this.beverages.stream()
                .mapToInt(Beverage::getPrice)
                .sum();
    }

    /*
    * 테스트 어려운 코드
    * 랜덤값, 현재 시간 값 등 Input 의 예측 불가능성
    */
    public CafeOrder createOrder() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalTime currentTime = currentDateTime.toLocalTime();
        if (currentTime.isBefore(SHOP_OPEN_TIME) || currentTime.isAfter(SHOP_CLOSE_TIME)) {
            throw new IllegalArgumentException("You cannot place an order between 07:00 and 18:00");
        }
        return new CafeOrder(LocalDateTime.now(), beverages);
    }

    /*
    * 테스트 용이한 코드
    * Input 의 의존성을 외부로 분리하여 예측 가능성.
    */
    public CafeOrder createOrder(LocalDateTime currentDateTime) {
        LocalTime currentTime = currentDateTime.toLocalTime();
        if (currentTime.isBefore(SHOP_OPEN_TIME) || currentTime.isAfter(SHOP_CLOSE_TIME)) {
            throw new IllegalArgumentException("You cannot place an order between 07:00 and 18:00");
        }
        return new CafeOrder(LocalDateTime.now(), beverages);
    }
}