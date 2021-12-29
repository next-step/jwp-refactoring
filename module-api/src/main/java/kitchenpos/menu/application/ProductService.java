package kitchenpos.menu.application;

import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import kitchenpos.menu.repository.ProductRepository;
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
    public ProductResponse create(final ProductRequest request) {
        Product save = productRepository.save(request.toEntity());
        return ProductResponse.of(save);
    }

    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();
        return ProductResponse.ofList(products);
    }
}
