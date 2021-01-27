package kitchenpos.product.application;

import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
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
        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }

    public Price getSumPrice(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        int sum = products.stream()
                .mapToInt(it -> it.getPrice().intValue())
                .sum();
        return Price.of(sum);
    }

    public Product findById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Product> findAllByIdIn(List<Long> productIds) {
        return productRepository.findAllByIdIn(productIds);
    }
}
