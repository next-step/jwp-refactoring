package kitchenpos.application;

import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product persistProduct = productRepository.save(productRequest.toEntity());
        return ProductResponse.of(persistProduct);
    }

    public List<ProductResponse> list() {
        return ProductResponse.listOf(productRepository.findAll());
    }
}
