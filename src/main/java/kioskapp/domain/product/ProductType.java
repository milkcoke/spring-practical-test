package kioskapp.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Getter
@RequiredArgsConstructor
public enum ProductType {
    HANDMADE("제조 음료"),
    BOTTLE("병음료"),
    BAKERY("빵");

    private final String description;

    public static boolean hasStock(ProductType type) {
        return List.of(BOTTLE, BAKERY).contains(type);
    }
}
