package kitchenpos.product.application;

import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        List<Product> products = productIds.stream()
                .map(it -> productRepository.findById(it).orElseThrow(IllegalArgumentException::new))
                .collect(Collectors.toList());

        int sum = products.stream()
                .mapToInt(it -> it.getPrice().intValue())
                .sum();
        return Price.of(sum);
    }

    public Product findById(Long productId) {
        return productRepository.findById(productId).get();
    }
}
