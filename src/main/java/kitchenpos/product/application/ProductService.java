package kitchenpos.product.application;

import java.util.List;

import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.product.repository.ProductDao;
import kitchenpos.product.domain.Product;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final Product product) {
        return ProductResponse.of(productDao.save(product));
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
