package kitchenpos.menu.ui;

import kitchenpos.menu.aplication.ProductService;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductRequest product) {
        final ProductResponse saveProduct = productService.saveProduct(product);
        final URI uri = URI.create("/api/products/" + saveProduct.getId());
        return ResponseEntity.created(uri)
                .body(saveProduct);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAllProduct() {
        return ResponseEntity.ok()
                .body(productService.findAllProduct());
    }
}
