package kioskapp.respository.product;

import kioskapp.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static kioskapp.domain.product.ProductSellingStatus.*;
import static kioskapp.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
//@SpringBootTest // Scan all components
// Transactional 에 의해 자동 Rollback
// Repository 관련 Components 만 load
@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    Product americano = Product.builder()
            .productNumber("001")
            .type(HANDMADE)
            .sellingStatus(SELLING)
            .name("아메리카노")
            .price(4000)
            .build();

    Product cafeLatte = Product.builder()
            .productNumber("002")
            .type(HANDMADE)
            .sellingStatus(HOLD)
            .name("카페라떼")
            .price(4500)
            .build();

    Product pineappleBread = Product.builder()
            .productNumber("003")
            .type(BAKERY)
            .sellingStatus(STOP)
            .name("소보로빵")
            .price(1500)
            .build();


    @Test
    @DisplayName("판매중, 판매 보류 상품만 조회한다.")
    void findAllBySellingStatusIn() {
        // given
        productRepository.saveAll(List.of(americano, cafeLatte, pineappleBread));

        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        // then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }

    @Test
    @DisplayName("판매 중단 상품핀 조회한다.")
    void findAllBySellingStatusIs() {
        // given
        productRepository.saveAll(List.of(americano, cafeLatte, pineappleBread));
        // when
        List<Product> products = productRepository.findAllBySellingStatusIs(STOP);

        // then
        assertThat(products).hasSize(1)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .containsOnly(
                        tuple("003", BAKERY, STOP, "소보로빵", 1500)
                );
    }

    @Test
    @DisplayName("상품 번호 리스트와 일치하는 상품 조회")
    void findAllByProductNumberIn() {
        // given
        productRepository.saveAll(List.of(americano, cafeLatte, pineappleBread));
        // when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("002", "003"));
        // then
        assertThat(products).hasSize(2)
                .extracting("name", "price")
                .containsExactlyInAnyOrder(
                        tuple("카페라떼", 4500),
                        tuple("소보로빵", 1500)
                );
    }
}