package kioskapp.controller.product;

import kioskapp.domain.product.ProductSellingStatus;
import kioskapp.service.product.ProductService;
import kioskapp.service.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/api/v1/products")
    public List<ProductResponse> getProducts(@RequestParam(required = false) ProductSellingStatus sellingStatus) {

        if (sellingStatus == null) {
            return this.productService.getAllProducts();
        }

        switch (sellingStatus) {
            case SELLING, HOLD -> {
                return this.productService.getSellingProducts();
            }
            case STOP -> {
                return this.productService.getNotSellingProducts();
            }
            default -> {
                return this.productService.getAllProducts();
            }
        }
    }
}
