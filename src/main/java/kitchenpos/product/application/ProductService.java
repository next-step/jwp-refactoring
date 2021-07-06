package kitchenpos.product.application;

import java.util.List;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        return ProductResponse.of(productDao.save(productRequest.toProduct()));
    }

    public List<ProductResponse> list() {
        return ProductResponse.listOf(productDao.findAll());
    }
}
