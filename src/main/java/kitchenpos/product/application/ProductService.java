package kitchenpos.product.application;


import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository repository;

    public ProductService(final ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        Product product = repository.save(request.toProduct());
        return ProductResponse.of(product);
    }

    public List<ProductResponse> list() {
        return ProductResponse.of(repository.findAll());
    }
}
