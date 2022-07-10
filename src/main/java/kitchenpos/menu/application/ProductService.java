package kitchenpos.menu.application;

import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(final ProductRequest productRequest) {
        final Product persistProduct = productRepository.save(
                new Product(productRequest.getName(), productRequest.getPrice()));

        return ProductResponse.from(persistProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return ProductResponse.fromToList(productRepository.findAll());
    }
}
