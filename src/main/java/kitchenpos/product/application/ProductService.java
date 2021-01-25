package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(final ProductRequest request) {
        Product product = request.toProduct();
        Product resultProcut = productRepository.save(product);
        return ProductResponse.of(resultProcut);
    }

    public List<ProductResponse> list() {
        final List<Product> products = productRepository.findAll();
        return products.stream().map(product -> ProductResponse.of(product))
                .collect(Collectors.toList());
    }
}
