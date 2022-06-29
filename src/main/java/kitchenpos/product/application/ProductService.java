package kitchenpos.product.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final Product product) {
        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }

    public Product findProductById(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
