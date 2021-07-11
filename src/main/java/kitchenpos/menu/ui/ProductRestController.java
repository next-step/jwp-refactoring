package kitchenpos.menu.ui;

import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest productRequest) {
        ProductResponse productResponses = productService.create(productRequest);
        return ResponseEntity
                .created(URI.create("/api/products/" + productResponses.getId()))
                .body(productResponses);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> list() {
        List<ProductResponse> productResponses = productService.list();
        return ResponseEntity.ok().body(productResponses);
    }
}
