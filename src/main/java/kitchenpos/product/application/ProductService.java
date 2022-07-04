package kitchenpos.product.application;

import kitchenpos.product.dao.ProductRepository;
import kitchenpos.product.domain.Products;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductCreateRequest request) {
        return ProductResponse.from(
                productRepository.save(request.of())
        );
    }

    public List<ProductResponse> list() {
        return new Products(productRepository.findAll()).toResponse();
    }

    public ProductResponse getProduct(final Long id) {
        return ProductResponse.from(
                productRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException(id + " 에 해당하는 상품을 찾을 수 없습니다."))
        );
    }
}
