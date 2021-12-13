package kitchenpos.product.application;

import java.util.List;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.ui.request.ProductRequest;
import kitchenpos.product.ui.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(ProductRequest request) {
        return ProductResponse.from(productRepository.save(request.toEntity()));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return ProductResponse.listFrom(productRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Product findById(long id) {
        return productRepository.findById(id)
            .orElseThrow(
                () -> new NotFoundException(String.format("상품 id(%d)를 찾을 수 없습니다.", id)));
    }
}
