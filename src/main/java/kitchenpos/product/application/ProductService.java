package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final Product productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice());
        product.validationCheck();
        productRepository.save(product);
        return ProductResponse.of(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
