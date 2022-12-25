package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(final ProductRequest request) {
        return ProductResponse.of(productRepository.save(new Product(request.getName(), request.getPrice())));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return ProductResponse.toResponselist(productRepository.findAll());
    }
}
