package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductDao productDao;

    @Autowired
    private ProductRepository productRepository;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public Product create(final Product product) {
        return productDao.save(product);
    }

    @Transactional
    public ProductResponse createV2(final ProductRequest request) {
        return ProductResponse.of(productRepository.save(request.toProduct()));
    }

    public List<Product> list() {
        return productDao.findAll();
    }

    public List<ProductResponse> listV2() {
        return ProductResponse.of(productRepository.findAll());
    }
}
