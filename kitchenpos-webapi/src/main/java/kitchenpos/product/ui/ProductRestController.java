package kitchenpos.product.ui;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.utils.ResponseUtils;

@RequestMapping(ProductRestController.BASE_URL)
@RestController
public class ProductRestController {

	public static final String BASE_URL = "/api/products";

	private final ProductService productService;

	public ProductRestController(final ProductService productService) {
		this.productService = productService;
	}

	@PostMapping
	public ResponseEntity<ProductResponse> create(@RequestBody final ProductRequest.Create request) {
		ProductResponse product = productService.create(request);
		return ResponseEntity.created(ResponseUtils.createdUrl(BASE_URL, product.getId()))
			.body(product);
	}

	@GetMapping
	public ResponseEntity<List<ProductResponse>> getList() {
		return ResponseEntity.ok()
			.body(productService.getList());
	}

}
