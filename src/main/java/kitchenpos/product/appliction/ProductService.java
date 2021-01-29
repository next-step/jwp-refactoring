package kitchenpos.product.appliction;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(final ProductRequest productRequest) {
        Product product = productRequest.toProduct();
        product.validateProductByPrice();

        return ProductResponse.of(this.productRepository.save(product));
    }


    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return this.productRepository.findAll().stream()
                .map(ProductResponse::of).collect(Collectors.toList());
    }
}
