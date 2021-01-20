package kitchenpos.application;

import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(ProductRequest request) {
        Product product = productRepository.save(toEntity(request));
        return fromEntity(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    private Product toEntity(ProductRequest request) {
        return new Product(request.getName(), request.getPrice());
    }

    private ProductResponse fromEntity(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }
}
