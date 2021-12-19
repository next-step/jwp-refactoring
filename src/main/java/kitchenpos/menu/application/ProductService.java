package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.ProductDao;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductRequest product) {
        final BigDecimal price = product.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        Product savedProduct = productDao.save(product.toEntity());
        return new ProductResponse(savedProduct);
    }

    public List<ProductResponse> list() {
        List<Product> products = productDao.findAll();
        return products.stream()
            .map(ProductResponse::new)
            .collect(Collectors.toList());
    }
}
