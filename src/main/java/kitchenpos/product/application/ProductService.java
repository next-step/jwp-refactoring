package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        return ProductResponse.from(productRepository.save(request.toProduct()));
    }

    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
            .map(ProductResponse::from)
            .collect(Collectors.toList());
    }

    public Product findById(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(IllegalArgumentException::new);
    }
}
