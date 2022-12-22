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

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product product = productRequest.toProduct();
        return ProductResponse.of(productRepository.save(product));
    }

    public List<ProductResponse> list() {
        List<Product> persistProducts = productRepository.findAll();
        return persistProducts.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
