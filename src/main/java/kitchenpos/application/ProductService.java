package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(final ProductRequest request) {
        return productRepository.save(request.toProduct());
    }

    @Transactional(readOnly = true)
    public List<Product> list() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product getById(final Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("상품을 찾을 수 없습니다. id: %d", id)));
    }
}
