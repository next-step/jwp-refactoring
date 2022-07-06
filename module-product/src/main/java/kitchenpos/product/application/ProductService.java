package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        final Product product = productRequest.toProduct();
        final Product persist = productRepository.save(product);
        return persist.toProductResponse();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
                .map(Product::toProductResponse)
                .collect(Collectors.toList());
    }
}
