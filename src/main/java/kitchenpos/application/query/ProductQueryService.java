package kitchenpos.application.query;

import kitchenpos.common.exception.EntityNotExistsException;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.response.ProductViewResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductQueryService {
    private final ProductRepository productRepository;

    public ProductQueryService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductViewResponse> list() {
        return productRepository.findAll()
                .stream()
                .map(ProductViewResponse::of)
                .collect(Collectors.toList());
    }

    public ProductViewResponse findById(Long id) {
        return ProductViewResponse.of(productRepository.findById(id)
                .orElseThrow(EntityNotExistsException::new));
    }
}
