package kitchenpos.product;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.dto.product.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final Product product) {
        return ProductResponse.from(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productRepository.findAll()
            .stream()
            .map(ProductResponse::from)
            .collect(toList());
    }
}
