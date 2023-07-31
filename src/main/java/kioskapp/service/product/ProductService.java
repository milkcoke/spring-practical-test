package kioskapp.service.product;

import kioskapp.domain.product.Product;
import kioskapp.domain.product.ProductSellingStatus;
import kioskapp.respository.product.ProductRepository;
import kioskapp.service.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductResponse> getAllProducts() {
        var products = this.productRepository.findAll();
        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
    /**
     * 판매 중인 상품 목록 반환
     */
    public List<ProductResponse> getSellingProducts() {
        List<Product> products = this.productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    /**
     * 판매 중지된 상품 목록 반환
     */
    public List<ProductResponse> getNotSellingProducts() {
        List<Product> products = this.productRepository.findAllBySellingStatusIs(ProductSellingStatus.STOP);

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
