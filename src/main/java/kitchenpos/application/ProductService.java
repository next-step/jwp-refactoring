package kitchenpos.application;

import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse createProduct(final ProductCreateRequest request) {
        return ProductResponse.of(productRepository.save(request.toProduct()));
    }

    public List<ProductResponse> findAll() {
        return ProductResponse.list(productRepository.findAll());
    }
}
