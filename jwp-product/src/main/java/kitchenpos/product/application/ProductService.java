package kitchenpos.product.application;

import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.exception.EmptyProductException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        final Product persistProduct = productRepository.save(productRequest.toProduct());
        return ProductResponse.from(persistProduct);
    }

    public List<ProductResponse> list() {
        final List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    public Product getById(final Long id) {
        return productRepository.findById(id)
                .orElseThrow(EmptyProductException::new);
    }
}
