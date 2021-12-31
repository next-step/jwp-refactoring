package kitchenpos.product.application;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        final Product persistProduct = productRepository.save(productRequest.toProduct());

        return ProductResponse.of(persistProduct);
    }

    public ProductResponse findById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품을 찾을 수 없습니다."));

        return ProductResponse.of(product);
    }

    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
