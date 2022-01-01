package kitchenpos.product.application;

import java.util.List;
import kitchenpos.moduledomain.common.exception.NoResultDataException;
import kitchenpos.moduledomain.product.Product;
import kitchenpos.moduledomain.product.ProductDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductDao productDao;


    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(final Product product) {
        return productDao.save(product);
    }

    public List<Product> list() {
        return productDao.findAll();
    }

}
