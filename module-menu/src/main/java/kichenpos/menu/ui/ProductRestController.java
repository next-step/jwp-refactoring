package kichenpos.menu.ui;

import kichenpos.menu.application.ProductService;
import kichenpos.menu.ui.dto.ProductCreateRequest;
import kichenpos.menu.ui.dto.ProductCreateResponse;
import kichenpos.menu.ui.dto.ProductResponse;
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
    public ResponseEntity<ProductCreateResponse> create(@RequestBody final ProductCreateRequest request) {
        final ProductCreateResponse created = new ProductCreateResponse(productService.create(request.toEntity()));
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        return ResponseEntity.ok()
                .body(productService.list()
                        .stream()
                        .map(ProductResponse::new)
                        .collect(Collectors.toList())
                )
                ;
    }
}
