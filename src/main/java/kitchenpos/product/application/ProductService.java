package kitchenpos.product.application;


import kitchenpos.common.model.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
public class ProductService {
    public static final String NOT_EXIST_PRODUCT = "존재하지 않는 상품입니다 : ";
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        Product savedProduct = productRepository.save(request.toEntity());
        return new ProductResponse(savedProduct);
    }

    public List<ProductResponse> list() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::new)
                .collect(toList());
    }

    public Price getProductPrice(Long productId, Long quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(NOT_EXIST_PRODUCT + productId));

        return product.getTotalPrice(quantity);
    }
}
