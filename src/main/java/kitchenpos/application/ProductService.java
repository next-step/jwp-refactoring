package kitchenpos.application;

import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productDao) {
        this.productRepository = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductRequest product) {

        return ProductResponse.of(productRepository.save(product.toProduct()));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return ProductResponse.toList(productRepository.findAll());
    }
}
