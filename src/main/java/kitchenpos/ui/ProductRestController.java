package kitchenpos.ui;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.ProductRequest;
import kitchenpos.ui.dto.ProductResponse;
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
    public ResponseEntity<Product> create(@RequestBody final Product product) {
        final Product created = productService.create(product);
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @PostMapping("/api/products2")
    public ResponseEntity<ProductResponse> create2(@RequestBody final ProductRequest request) {
        final Product created = productService.create(request.toProduct());
        final URI uri = URI.create("/api/products2/" + created.getId());
        return ResponseEntity.created(uri)
                .body(ProductResponse.of(created))
                ;
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<Product>> list() {
        return ResponseEntity.ok()
                .body(productService.list())
                ;
    }

    @GetMapping("/api/products2")
    public ResponseEntity<List<ProductResponse>> list2() {
        return ResponseEntity.ok()
                .body(ProductResponse.ofList(productService.list()))
                ;
    }
}
