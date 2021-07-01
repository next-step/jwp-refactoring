package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductCreate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(final ProductCreate productCreate) {
        return productDao.save(new Product(productCreate.getName(), productCreate.getPrice()));
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
