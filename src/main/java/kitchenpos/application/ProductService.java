package kitchenpos.application;

import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductCreate;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductCreate productCreate) {
        return productRepository.save(Product.from(productCreate));
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
