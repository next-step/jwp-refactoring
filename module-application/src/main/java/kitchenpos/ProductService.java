package kitchenpos;

import dto.product.ProductRequest;
import dto.product.ProductResponse;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
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
    public ProductResponse create(final ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice());
        Product saved = productRepository.save(product);

        return ProductResponse.of(saved);
    }

    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
