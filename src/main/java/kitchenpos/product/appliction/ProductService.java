package kitchenpos.product.appliction;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(final Product product) {
        product.validateProductByPrice();

        return this.productRepository.save(product);
    }


    @Transactional(readOnly = true)
    public List<Product> list() {
        return this.productRepository.findAll();
    }
}
