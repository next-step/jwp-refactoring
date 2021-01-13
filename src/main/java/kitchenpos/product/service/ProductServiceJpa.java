package kitchenpos.product.service;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class ProductServiceJpa {
    private final ProductRepository productRepository;

    public ProductServiceJpa(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(final ProductRequest productRequest) {
        return productRepository.save(ProductRequest.toProduct(productRequest));
    }

    @Transactional(readOnly = true)
    public List<Product> list() {
        return productRepository.findAll();
    }
}
