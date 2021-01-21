package kitchenpos.application.product;

import kitchenpos.domain.product.Product;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
import kitchenpos.repository.product.ProductRepository;
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
    public ProductResponse create(final ProductRequest product) {
        Product savedProduct = productRepository.save(product.toProduct());
        return ProductResponse.of(savedProduct);
    }

    public List<ProductResponse> findAll() {
        return ProductResponse.ofList(productRepository.findAll());
    }
}
