package kitchenpos.application;

import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;

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
    public Product create(final Product product) {
        final Price price = product.getPrice();


        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
