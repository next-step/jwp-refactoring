package kitchenpos.product.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

@RestController
public class ProductRestController {
	private final ProductService productService;

	public ProductRestController(final ProductService productService) {
		this.productService = productService;
	}

	@PostMapping("/api/products")
	public ResponseEntity<ProductResponse> create(@RequestBody final ProductRequest productRequest) {
		ProductResponse productResponse = productService.create(productRequest);
		final URI uri = URI.create("/api/products/" + productResponse.getId());
		return ResponseEntity.created(uri)
			.body(productResponse);
	}

	@GetMapping("/api/products")
	public ResponseEntity<List<ProductResponse>> list() {
		return ResponseEntity.ok()
			.body(productService.list());
	}
}
