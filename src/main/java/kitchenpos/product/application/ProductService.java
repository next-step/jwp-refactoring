package kitchenpos.product.application;

import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.domain.ProductRepository;
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
