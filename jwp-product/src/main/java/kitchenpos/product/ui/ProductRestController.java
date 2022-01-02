package kitchenpos.product.ui;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/api/products", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductRequest productRequest) {
        final ProductResponse created = productService.create(productRequest);
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping(value = "/api/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponse>> list() {
        return ResponseEntity.ok()
                .body(productService.list());
    }
}
