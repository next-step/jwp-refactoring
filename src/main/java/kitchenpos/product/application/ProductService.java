package kitchenpos.product.application;

import java.util.List;
import kitchenpos.common.exception.NoResultDataException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductDao;
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

    public Product findByIdThrow(Long id) {
        return productDao.findById(id)
            .orElseThrow(NoResultDataException::new);
    }

}
