package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.ui.dto.ProductRequest;
import kitchenpos.menu.ui.dto.ProductResponse;

@RestController
public class ProductRestController {
	private final ProductService productService;

	public ProductRestController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping("/api/products")
	public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest productRequest) {
		Product createdProduct = productService.create(productRequest.toProduct());

		return ResponseEntity
			.created(createUri(createdProduct))
			.body(new ProductResponse(createdProduct));
	}

	@GetMapping("/api/products")
	public ResponseEntity<List<ProductResponse>> list() {
		return ResponseEntity
			.ok()
			.body(ProductResponse.of(productService.findAll()));
	}

	private URI createUri(Product createdProduct) {
		return URI.create("/api/products/" + createdProduct.getId());
	}
}
