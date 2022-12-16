package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;

@RestController
public class ProductRestController {
	private final ProductService productService;

	public ProductRestController(final ProductService productService) {
		this.productService = productService;
	}

	@PostMapping("/api/products")
	public ResponseEntity<ProductResponse> create(@RequestBody final ProductRequest productRequest) {
		final ProductResponse created = productService.create(productRequest);
		final URI uri = URI.create("/api/products/" + created.getId());
		return ResponseEntity.created(uri).body(created);
	}

	@GetMapping("/api/products")
	public ResponseEntity<List<ProductResponse>> list() {
		return ResponseEntity.ok().body(productService.list());
	}

}
