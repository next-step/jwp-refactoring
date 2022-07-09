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

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product product = productRepository.save(productRequest.toProduct());
        return ProductResponse.from(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();
        return ProductResponse.toProductResponses(products);
    }
}
