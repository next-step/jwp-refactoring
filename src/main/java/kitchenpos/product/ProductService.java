package kitchenpos.product;

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
    public Product create(final Product product) {
        product.checkValidation();
        return productDao.save(product);
    }

    public List<Product> list() {
        return productDao.findAll();
    }

}
