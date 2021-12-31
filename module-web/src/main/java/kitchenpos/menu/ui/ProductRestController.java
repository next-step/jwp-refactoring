package kitchenpos.menu.ui;

import java.net.*;
import java.util.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import kitchenpos.menu.application.*;
import kitchenpos.menu.dto.*;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductRequest product) {
        final ProductResponse saveProduct = productService.save(product);
        final URI uri = URI.create("/api/products/" + saveProduct.getId());
        return ResponseEntity.created(uri)
                .body(saveProduct);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAllProduct() {
        return ResponseEntity.ok()
                .body(productService.findAll());
    }
}
