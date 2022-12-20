package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        return ProductResponse.of(productRepository.save(new Product(request.getName(), request.getPrice())));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return ProductResponse.list(productRepository.findAll());
    }
}
