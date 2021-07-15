package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(final ProductRequest productRequest) {
        return ProductResponse.from(productRepository.save(
                new Product(productRequest.getName(), BigDecimal.valueOf(productRequest.getPrice()))));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
