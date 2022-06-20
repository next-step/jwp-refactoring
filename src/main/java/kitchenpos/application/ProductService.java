package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        return new ProductResponse(productDao.save(request.toProduct()));
    }

    public List<ProductResponse> list() {
        return productDao.findAll().stream().map(ProductResponse::new).collect(Collectors.toList());
    }
}
