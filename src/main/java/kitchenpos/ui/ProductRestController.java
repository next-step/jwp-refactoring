package kitchenpos.ui;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductCreate;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductViewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductViewResponse> create(@RequestBody final ProductCreateRequest productCreateRequest) {
        final Product created = productService.create(
                new ProductCreate(
                        productCreateRequest.getName(),
                        new Price(productCreateRequest.getPrice())
                )
        );
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri)
                .body(ProductViewResponse.of(created));
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductViewResponse>> list() {
        List<ProductViewResponse> results = productService.list()
                .stream()
                .map(ProductViewResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(results);
    }
}
