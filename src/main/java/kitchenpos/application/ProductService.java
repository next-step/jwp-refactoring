package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final Product product) {
        product.validatePrice();
        return ProductResponse.of(productDao.save(product));
    }

    public List<ProductResponse> list() {
        return productDao.findAll().stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
