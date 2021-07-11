package kitchenposNew.menu.application;

import kitchenposNew.menu.domain.Product;
import kitchenposNew.menu.domain.ProductRepository;
import kitchenposNew.menu.dto.ProductRequest;
import kitchenposNew.menu.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product persistProduct = productRepository.save(productRequest.toProduct());
        return ProductResponse.of(persistProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
                .map(product -> ProductResponse.of(product))
                .collect(Collectors.toList());
    }
}
