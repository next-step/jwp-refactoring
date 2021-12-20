package kitchenpos.product.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductDto;

@RestController(value = "ToBeProductRestController")
public class ProductRestController {
	private final ProductService productService;

	public ProductRestController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping("/api/products")
	public ResponseEntity<ProductDto> create(@RequestBody ProductCreateRequest request) {
		ProductDto product = productService.create(request);
		URI uri = URI.create("/api/products/" + product.getId());
		return ResponseEntity.created(uri).body(product);
	}

	@GetMapping("/api/products")
	public ResponseEntity<List<ProductDto>> list() {
		List<ProductDto> products = productService.list();
		return ResponseEntity.ok().body(products);
	}
}
