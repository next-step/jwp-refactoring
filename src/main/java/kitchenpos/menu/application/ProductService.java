package kitchenpos.menu.application;

import kitchenpos.menu.dao.ProductDao;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.ProductRequest;
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
    public Product create(final ProductRequest productRequest) {
        return productDao.save(ProductRequest.to(productRequest));
    }

    public List<Product> list() {
        return productDao.findAll();
    }
}
