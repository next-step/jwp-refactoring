package kitchenpos.product.application;

import kitchenpos.product.dao.ProductRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.Products;
import kitchenpos.product.dto.ProductCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductCreateRequest request) {
        return productRepository.save(request.of());
    }

    public Products list() {
        return new Products(productRepository.findAll());
    }

    public Product getProduct(final Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 에 해당하는 상품을 찾을 수 없습니다."));
    }
}
