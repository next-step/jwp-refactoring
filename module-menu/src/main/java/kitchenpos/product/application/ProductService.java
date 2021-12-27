package kitchenpos.product.application;

import java.util.List;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productDao) {
        this.productRepository = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductRequest product) {

        return ProductResponse.of(productRepository.save(product.toProduct()));
    }

    public List<ProductResponse> list() {
        return ProductResponse.toList(productRepository.findAll());
    }
}
