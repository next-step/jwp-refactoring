package kitchenpos.application.product;


import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(final ProductRequest productRequest) {
        return ProductResponse.of(productRepository.save(productRequest.toProduct()));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productRepository.findAll()
                                .stream()
                                .map(ProductResponse::of)
                                .collect(Collectors.toList());
    }
}
