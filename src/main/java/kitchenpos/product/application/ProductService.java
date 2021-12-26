package kitchenpos.product.application;

import kitchenpos.common.exception.NoResultDataException;
import kitchenpos.product.domain.ProductDao;
import kitchenpos.product.domain.Product;
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
