package kioskapp.respository.product;

import kioskapp.RepositoryTestSupport;
import kioskapp.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kioskapp.domain.product.ProductSellingStatus.*;
import static kioskapp.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class ProductRepositoryTest extends RepositoryTestSupport {
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

    @Test
    @DisplayName("가장 마지막 상품의 번호를 조회해온다.")
    void findLatestProduct() {
        // given
        productRepository.saveAll(List.of(americano, cafeLatte, pineappleBread));
        // when
        var lastProductNumber = productRepository.findLatestProductNumber();
        // then
        assertThat(lastProductNumber).isEqualTo("003");
    }

    @Test
    @DisplayName("가장 마지막 상품 번호 조회시 아무 상품이 없으면 null 을 반환한다.")
    void findLatestNotExistProduct() {
        // given // when
        var lastProductNumber = productRepository.findLatestProductNumber();
        // then
        assertThat(lastProductNumber).isNull();
    }
}