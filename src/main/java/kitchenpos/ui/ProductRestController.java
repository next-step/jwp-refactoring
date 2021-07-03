package kitchenpos.ui;

import kitchenpos.application.ProductService;
import kitchenpos.domain.menu.Product;
import kitchenpos.dto.menu.ProductRequest;
import kitchenpos.dto.menu.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductRequest productRequest) {
        final Product created = productService.create(productRequest);
        final URI uri = URI.create("/api/products/" + created.getId());

        return ResponseEntity.created(uri)
                .body(ProductResponse.of(created));
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        List<Product> list = productService.list();
        return ResponseEntity.ok()
                .body(list.stream()
                        .map(ProductResponse::of)
                        .collect(Collectors.toList())
                );
    }
}
