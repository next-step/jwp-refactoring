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

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        Product product = productRepository.save(toEntity(request));
        return ProductResponse.of(product);
    }

    private Product toEntity(final ProductRequest request) {
        return new Product(request.getName(), request.getPrice());
    }

    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
                .map(product -> ProductResponse.of(product))
                .collect(Collectors.toList());
    }
}
