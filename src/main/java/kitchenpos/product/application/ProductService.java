package kitchenpos.product.application;

import java.util.stream.Collectors;
import kitchenpos.product.dao.ProductDao;
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
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public ProductResponse create(final ProductRequest productRequest) {
        final BigDecimal price = productRequest.getPrice();
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        return ProductResponse.of(productDao.save(productRequest.toProduct()));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productDao.findAll()
                         .stream()
                         .map(ProductResponse::of)
                         .collect(Collectors.toList());
    }
}
