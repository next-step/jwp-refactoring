package kitchenpos.product.application;

import kitchenpos.product.exception.NoSuchProductException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product savedProduct = productRepository.save(Product.of(productRequest.getName(), productRequest.getPrice()));
        return ProductResponse.from(savedProduct);
    }

    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();
        return ProductResponse.asListFrom(products);
    }

    public Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchProductException(productId));
    }
}
