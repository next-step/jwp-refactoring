package kitchenpos.menu.application;

import java.util.stream.Collectors;
import kitchenpos.menu.domain.ProductEntity;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.domain.request.ProductRequest;
import kitchenpos.menu.domain.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        ProductEntity product = ProductEntity.of(productRequest.getName(), productRequest.getPrice());
        product = productRepository.save(product);
        return ProductResponse.of(product);
    }

    public List<ProductResponse> list() {
        List<ProductEntity> products = productRepository.findAll();
        return products.stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }
}
