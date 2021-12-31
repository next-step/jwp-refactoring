package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    private static final String BASE_PATH = "/api/products/";

    private final ProductService productService;

    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(BASE_PATH)
    public ResponseEntity<ProductResponse> register(@RequestBody final ProductRequest request) {
        final ProductResponse response = productService.register(request);
        final URI uri = URI.create(BASE_PATH + response.getId());
        return ResponseEntity.created(uri)
            .body(response);
    }

    @GetMapping(BASE_PATH)
    public ResponseEntity<List<ProductResponse>> list() {
        return ResponseEntity.ok()
            .body(productService.list());
    }
}
