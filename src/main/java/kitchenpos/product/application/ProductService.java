package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductDao;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductResponse;
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
    public ProductResponse create(final ProductCreateRequest request) {
        Product product = request.toEntity();
        Product saveProduct = productDao.save(product);
        return ProductResponse.of(saveProduct);
    }

    public List<ProductResponse> list() {
        List<Product> products = productDao.findAll();
        return ProductResponse.ofList(products);
    }
}
