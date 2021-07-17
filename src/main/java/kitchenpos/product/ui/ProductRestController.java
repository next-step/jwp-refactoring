package kitchenpos.product.ui;

import kitchenpos.product.application.ProductService;
import kitchenpos.domain.ProductRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    public ResponseEntity<ProductRequest> create(@RequestBody final ProductRequest product) {
        final ProductRequest created = productService.create(product);
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductRequest>> list() {
        return ResponseEntity.ok()
                .body(productService.list())
                ;
    }

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity handleRuntimeException(IllegalArgumentException illegalArgumentException) {
		return ResponseEntity.badRequest().build();
	}
}
