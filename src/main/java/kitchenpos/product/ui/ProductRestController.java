package kitchenpos.product.ui;

import kitchenpos.product.application.ProductService;
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
    private static final String BASE_URL = "/api/products";

    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(BASE_URL)
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductRequest productRequest) {
        final ProductResponse created = productService.create(productRequest);
        final URI uri = URI.create(BASE_URL + created.getId());

        return ResponseEntity.created(uri)
                .body(created);
    }

    @GetMapping(BASE_URL)
    public ResponseEntity<List<ProductResponse>> list() {
        return ResponseEntity.ok()
                .body(productService.list());
    }
}
