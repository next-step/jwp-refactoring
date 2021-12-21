package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
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
    public ProductResponse create(final ProductRequest product) {

        return ProductResponse.of(productDao.save(product.toProduct()));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return ProductResponse.toList(productDao.findAll());
    }
}
