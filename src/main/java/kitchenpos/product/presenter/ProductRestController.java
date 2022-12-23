package kitchenpos.product.presenter;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductRequest request) {
        ProductResponse result = productService.create(request);
        final URI uri = URI.create("/api/products/" + result.getId());
        return ResponseEntity.created(uri).body(result);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<Product>> list() {
        return ResponseEntity.ok().body(productService.list());
    }
}
