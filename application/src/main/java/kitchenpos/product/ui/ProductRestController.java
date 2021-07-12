package kitchenpos.product.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.CreateProductRequest;
import kitchenpos.product.dto.ProductDto;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static java.util.stream.Collectors.toList;

@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductResponse> create(@RequestBody CreateProductRequest request) {
        ProductDto created = productService.create(request.getName(), request.getPrice());
        URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri)
                             .body(ProductResponse.of(created));
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        return ResponseEntity.ok()
                .body(productService.list()
                                    .stream()
                                    .map(ProductResponse::of)
                                    .collect(toList()));
    }
}
