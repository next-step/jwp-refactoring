package kitchenpos.application.product;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.product.domain.Product;
import kitchenpos.common.product.dto.ProductRequest;
import kitchenpos.common.product.dto.ProductResponse;
import kitchenpos.common.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        return ProductResponse.of(productRepository.save(request.toProduct()));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Product getById(final Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("상품을 찾을 수 없습니다. id: %d", id)));
    }
}
