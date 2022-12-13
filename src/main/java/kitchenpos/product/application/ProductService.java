package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static kitchenpos.common.Price.PRICE_MINIMUM_EXCEPTION_MESSAGE;
import static kitchenpos.common.Price.PRICE_NOT_NULL_EXCEPTION_MESSAGE;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductCreateRequest request) {
        final BigDecimal price = request.getPrice();

        if (Objects.isNull(price)) {
            throw new IllegalArgumentException(PRICE_NOT_NULL_EXCEPTION_MESSAGE);
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(PRICE_MINIMUM_EXCEPTION_MESSAGE);
        }

        return productRepository.save(request.toProduct());
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
