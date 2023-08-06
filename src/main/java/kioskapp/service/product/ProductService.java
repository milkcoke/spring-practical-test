package kioskapp.service.product;

import kioskapp.domain.product.Product;
import kioskapp.domain.product.ProductSellingStatus;
import kioskapp.respository.product.ProductRepository;
import kioskapp.service.product.dto.ProductCreateServiceRequest;
import kioskapp.service.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    // CRUD JPA Repository 기본 메소드는 @Transactional 이라 Service 클래스에 별도로 @Transactional 어노테이션이 필요하진 않다.
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

    @Transactional
    public ProductResponse createProduct(ProductCreateServiceRequest request) {
        // DB 에서 마지막 저장된 Product 의 상품 번호를 읽어서 + 1하려고 한다.
        // ex) 001 -> 002 , 009 -> 010

        String lastProductNumber = this.productRepository.findLatestProductNumber();
        String nextProductNumber = convertToNextProductNumber(lastProductNumber);
        Product nextProduct = request.toEntity(nextProductNumber);

        this.productRepository.save(nextProduct);
        return ProductResponse.of(nextProduct);
    }

    private String convertToNextProductNumber(String lastProductNumberStr) {
        if (lastProductNumberStr == null) {
            return "001";
        }
        int lastProductNumber = Integer.parseInt(lastProductNumberStr);
        int nextProductNumber = lastProductNumber + 1;
        return String.format("%03d", nextProductNumber);
    }
}
