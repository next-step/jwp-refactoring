package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product product = productRequest.toProduct();
        Product saveProduct = saveProduct(product);

        return ProductResponse.of(saveProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        List<Product> products = findProducts();

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    private Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    private List<Product> findProducts() {
        return productRepository.findAll();
    }
}
