package kichenpos.menu.application;

import kichenpos.menu.domain.Product;
import kichenpos.menu.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
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
}
