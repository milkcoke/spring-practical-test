package kioskapp.controller.product;

import jakarta.validation.Valid;
import kioskapp.common.ApiResponse;
import kioskapp.domain.product.ProductSellingStatus;
import kioskapp.service.product.ProductService;
import kioskapp.service.product.dto.ProductCreateRequest;
import kioskapp.service.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/api/v1/products")
    public ApiResponse<List<ProductResponse>> getProducts(@RequestParam(required = false) ProductSellingStatus sellingStatus) {

        if (sellingStatus == null) {
            return ApiResponse.ok(productService.getAllProducts());
        }

        switch (sellingStatus) {
            case SELLING, HOLD -> {
                return ApiResponse.ok(productService.getSellingProducts());
            }
            case STOP -> {
                return ApiResponse.ok(productService.getNotSellingProducts());
            }
            default -> {
                return ApiResponse.ok(productService.getAllProducts());
            }
        }
    }

    @PostMapping("/api/v1/products")
//    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProductResponse> postProduct(@Valid @RequestBody ProductCreateRequest request) {
        return ApiResponse.created(productService.createProduct(request));
    }
}
