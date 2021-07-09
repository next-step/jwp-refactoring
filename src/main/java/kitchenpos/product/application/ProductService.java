package kitchenpos.product.application;

import static kitchenpos.exception.KitchenposExceptionMessage.MENU_PRICE_CANNOT_OVER_THAN_PRODUCT_PRICE;

import java.util.stream.Collectors;
import kitchenpos.exception.KitchenposException;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ProductService {

    private static final BigDecimal MIN_PRICE = BigDecimal.ZERO;

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(final ProductRequest productRequest) {
        checkPriceGreaterThanMin(productRequest.getPrice());
        return ProductResponse.of(productRepository.save(productRequest.toProduct()));
    }

    private void checkPriceGreaterThanMin(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(MIN_PRICE) < 0) {
            throw new KitchenposException(MENU_PRICE_CANNOT_OVER_THAN_PRODUCT_PRICE);
        }
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productRepository.findAll()
                                .stream()
                                .map(ProductResponse::of)
                                .collect(Collectors.toList());
    }
}
