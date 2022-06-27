package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        Product product = productRepository.save(toEntity(request));
        return ProductResponse.of(product);
    }

    private Product toEntity(final ProductRequest request) {
        return new Product(request.getName(), request.getPrice());
    }

    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
                .map(product -> ProductResponse.of(product))
                .collect(Collectors.toList());
    }

    public Product findProductById(final Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("상품을 찾을 수 없습니다. id: " + id));
    }
}
