package kitchenpos.product.ui;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/products")
@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductRequest productRequest) {
        final ProductResponse product = productService.create(productRequest);
        final URI uri = URI.create("/api/products/" + product.getId());
        return ResponseEntity.created(uri).body(product);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> list() {
        return ResponseEntity.ok().body(productService.list());
    }
}
