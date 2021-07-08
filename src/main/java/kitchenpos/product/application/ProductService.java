package kitchenpos.product.application;

import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        final Product persistProduct = productRepository.save(new Product(productRequest.getName(), productRequest.getPrice()));
        return ProductResponse.from(persistProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return ProductResponse.fromToList(productRepository.findAll());
    }
}
