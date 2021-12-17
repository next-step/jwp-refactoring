package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
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
    public ProductResponse create(final ProductRequest productRequest) {
        final long price = productRequest.getPrice();

        if (price <= 0) {
            throw new IllegalArgumentException();
        }

        Product savedProduct = productDao.save(productRequest.toEntity());
        return ProductResponse.of(savedProduct);
    }

    public List<ProductResponse> list() {
        List<Product> products = productDao.findAll();
        return ProductResponse.ofList(products);
    }
}
