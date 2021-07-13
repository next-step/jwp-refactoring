package kitchenpos.product.service;

import java.util.List;
import kitchenpos.product.domain.entity.Product;
import kitchenpos.product.domain.entity.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(ProductRequest productRequest) {
        return ProductResponse.of(productRepository
            .save(new Product(productRequest.getName(), productRequest.getPrice())));
    }

    public List<ProductResponse> list() {
        return ProductResponse.ofList(productRepository.findAll());
    }
}
