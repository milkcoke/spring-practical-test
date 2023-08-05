package kioskapp.respository.product;

import kioskapp.domain.product.Product;
import kioskapp.domain.product.ProductSellingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * SELECT *
     * FROM product
     * WHERE selling_status in ('SELLING', 'HOLD');
     */
    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);
    List<Product> findAllBySellingStatusIs(ProductSellingStatus sellingStatus);
    List<Product> findAllByProductNumberIn(List<String> productNumbers);

    // Query DSL 의 Builder 패턴을 사용했든
    // Native Query 를 직접 정의했든
    // Spring Data JPA 의 쿼리 메소드를 썼든 테스트 쿼리는 필요하다.
    @Query(value = "SELECT p.product_number FROM product p ORDER BY id desc limit 1", nativeQuery = true)
    String findLatestProductNumber();
}