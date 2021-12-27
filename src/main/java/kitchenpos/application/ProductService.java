package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        return ProductResponse.from(productDao.save(request.toEntity()));
    }

    public List<ProductResponse> list() {
        List<Product> products = productDao.findAll();
        return products.stream()
            .map(ProductResponse::from)
            .collect(Collectors.toList());
    }
}
