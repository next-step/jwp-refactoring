package kitchenpos.product.application;

import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

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

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return ProductResponse.listOf(productRepository.findAll());
    }
}
