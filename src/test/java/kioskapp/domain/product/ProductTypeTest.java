package kioskapp.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ProductTypeTest {

    @Test
    @DisplayName("fail - 수제 음료는 재고를 갖지 않는 상품 종류다.")
    void handmadeNotHasStock() {
        // given
        ProductType givenType = ProductType.HANDMADE;
        // when
        var result = ProductType.hasStock(givenType);
        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("success - 병음료는 재고를 갖는 상품 종류다.")
    void bottleHasStock() {
        // given
        ProductType givenType = ProductType.BOTTLE;
        // when
        var result = ProductType.hasStock(givenType);
        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("success - 빵은 재고를 갖는 상품 종류다.")
    void bakeryHasStock() {
        // given
        ProductType givenType = ProductType.BAKERY;
        // when
        var result = ProductType.hasStock(givenType);
        // then
        assertThat(result).isTrue();
    }

}
