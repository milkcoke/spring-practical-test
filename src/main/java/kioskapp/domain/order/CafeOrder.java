package kioskapp.domain.order;

import kioskapp.domain.beverage.Beverage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CafeOrder {
    private final LocalDateTime orderDateTime;
    private final List<Beverage> beverages;
}
