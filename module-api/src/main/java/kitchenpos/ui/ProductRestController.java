package kitchenpos.ui;

import kitchenpos.application.command.ProductQueryService;
import kitchenpos.application.command.ProductService;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductViewResponse;
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
    private final ProductQueryService productQueryService;

    public ProductRestController(ProductService productService, ProductQueryService productQueryService) {
        this.productService = productService;
        this.productQueryService = productQueryService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductViewResponse> create(@RequestBody final ProductCreateRequest productCreateRequest) {
        final Long id = productService.create(productCreateRequest.toCreate());

        return ResponseEntity.created(URI.create("/api/products/" + id))
                .body(productQueryService.findById(id));
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductViewResponse>> list() {
        return ResponseEntity.ok()
                .body(productQueryService.list());
    }
}
