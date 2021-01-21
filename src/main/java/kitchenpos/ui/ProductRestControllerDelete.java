package kitchenpos.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.ProductServiceDelete;
import kitchenpos.domain.Product;

@RestController
@RequestMapping("/api/product")
public class ProductRestControllerDelete {
	private final ProductServiceDelete productServiceDelete;

	public ProductRestControllerDelete(final ProductServiceDelete productServiceDelete) {
		this.productServiceDelete = productServiceDelete;
	}

	@PostMapping
	public ResponseEntity<Product> create(@RequestBody final Product product) {
		final Product created = productServiceDelete.create(product);
		final URI uri = URI.create("/api/products/" + created.getId());
		return ResponseEntity.created(uri).body(created);
	}

	@GetMapping
	public ResponseEntity<List<Product>> list() {
		return ResponseEntity.ok().body(productServiceDelete.list());
	}
}
