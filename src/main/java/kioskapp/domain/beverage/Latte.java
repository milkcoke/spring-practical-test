package kioskapp.domain.beverage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Latte implements Beverage {
    private final String name = "Latte";
    private final int price = 4500;
}
