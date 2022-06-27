package kitchenpos.product.application;

import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.exception.NotFoundProductException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product savedProduct = productRepository.save(productRequest.toProduct());
        return ProductResponse.from(savedProduct);
    }

    public List<ProductResponse> list() {
        return ProductResponse.fromList(productRepository.findAll());
    }

    public Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundProductException(productId));
    }

    public List<Product> findProducts(List<Long> productIds) {
        return productRepository.findAllById(productIds);
    }
}
