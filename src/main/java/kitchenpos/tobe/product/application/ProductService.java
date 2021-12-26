package kitchenpos.tobe.product.application;

import java.util.List;
import kitchenpos.tobe.product.domain.Product;
import kitchenpos.tobe.product.domain.ProductRepository;
import kitchenpos.tobe.product.dto.ProductRequest;
import kitchenpos.tobe.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse register(final ProductRequest request) {
        final Product product = productRepository.save(request.toProduct());
        return ProductResponse.of(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return ProductResponse.ofList(productRepository.findAll());
    }
}
