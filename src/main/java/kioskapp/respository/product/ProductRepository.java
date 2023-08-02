package kioskapp.respository.product;

import kioskapp.domain.product.Product;
import kioskapp.domain.product.ProductSellingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
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
}