package kioskapp.domain.beverage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Americano implements Beverage {
    private final String name = "Americano";
    private final int price = 4000;
}
