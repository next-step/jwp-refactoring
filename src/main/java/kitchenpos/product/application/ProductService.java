package kitchenpos.product.application;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        Product savedProduct = productRepository.save(request.toEntity());
        return ProductResponse.of(savedProduct);
    }

    public List<ProductResponse> list() {
        return ProductResponse.of(productRepository.findAll());
    }

    public Product findByProductId(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    public List<Product> findByIdIn(List<Long> ids) {
        return productRepository.findByIdIn(ids);
    }
}
