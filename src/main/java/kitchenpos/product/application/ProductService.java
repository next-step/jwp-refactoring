package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.infra.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private static final String NOT_FOUND_PRODUCT_ERROR_MESSAGE = "해당 상품을 찾지 못하였습니다.";
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        final Product savedProduct = productRepository.save(request.toEntity());
        return ProductResponse.of(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
