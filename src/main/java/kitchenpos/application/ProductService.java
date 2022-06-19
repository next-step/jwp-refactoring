package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        final BigDecimal price = productRequest.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        Product savedProduct = productRepository.save(productRequest.toProduct());
        return ProductResponse.from(savedProduct);
    }

    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
