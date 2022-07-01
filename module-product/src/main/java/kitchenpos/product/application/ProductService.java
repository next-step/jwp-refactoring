package kitchenpos.product.application;

import kitchenpos.core.exception.CannotCreateException;
import kitchenpos.core.exception.ExceptionType;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.request.ProductRequest;
import kitchenpos.product.dto.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product product = Product.of(productRequest.getName(), productRequest.getPrice());
        product = productRepository.save(product);
        return ProductResponse.of(product);
    }

    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();
        return products.stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }

    public List<Product> findByIdIn(List<Long> productIds) {
        return productRepository.findByIdIn(productIds);
    }

    public Product findById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CannotCreateException(ExceptionType.NOT_EXIST_PRODUCT));
    }
}
