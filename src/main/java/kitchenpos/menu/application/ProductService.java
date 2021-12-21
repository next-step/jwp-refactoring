package kitchenpos.menu.application;

import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.ProductCreateRequest;
import kitchenpos.menu.dto.ProductResponse;
import kitchenpos.menu.infra.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductCreateRequest request) {
        final Product savedProduct = productRepository.save(request.toEntity());
        return ProductResponse.of(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
