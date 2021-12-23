package kitchenpos.application;

import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.exception.NoProductException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final Product product) {
        final Price price = product.getPrice();

        if (Objects.isNull(price) || price.compareTo(Price.Zero) < 0) {
            throw new IllegalArgumentException();
        }

        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }

    public Product findById(Long productId) {
        return productRepository.findById(productId).orElseThrow(NoProductException::new);
    }
}
