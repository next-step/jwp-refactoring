package kitchenpos.product.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.dto.ProductRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductRequest product) {
        final ProductResponse created = productService.create(product);
        return ResponseEntity.created(URI.create("/" + created.getId()))
                .body(created);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> list() {
        return ResponseEntity.ok()
                .body(productService.list());
    }
}
