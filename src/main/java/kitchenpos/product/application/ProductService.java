package kitchenpos.product.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.ui.request.ProductRequest;
import kitchenpos.product.ui.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        final BigDecimal price = request.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        return ProductResponse.from(productRepository.save(request.toEntity()));
    }

    public List<ProductResponse> list() {
        return ProductResponse.listFrom(productRepository.findAll());
    }
}
