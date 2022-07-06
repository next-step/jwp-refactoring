package kitchenpos.product.application;

import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.repository.ProductRepository;
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
        Product savedProduct = productRepository.save(Product.of(productRequest.getName(), productRequest.getPrice()));
        return ProductResponse.from(savedProduct);
    }

    public List<ProductResponse> list() {
        return ProductResponse.toProductResponses(productRepository.findAll());
    }

    public Product findByIdOrElseThrow(long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException());
    }

    public long findPriceByIdOrElseThrow(long productId) {
        Product product =  productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException());
        return product.getPrice();
    }
}
