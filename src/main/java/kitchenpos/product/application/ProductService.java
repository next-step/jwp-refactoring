package kitchenpos.product.application;

import java.util.stream.Collectors;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.infrastructure.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product product = Product.of(productRequest.getName(), BigDecimal.valueOf(productRequest.getPrice()));
        Product saved = productRepository.save(product);
        return ProductResponse.of(saved);
    }

    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
