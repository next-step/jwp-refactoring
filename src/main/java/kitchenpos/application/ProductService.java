package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
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
