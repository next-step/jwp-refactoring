package kitchenpos.product.application;

import kitchenpos.common.Price;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
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
        Product saved = productRepository.save(toProduct(productRequest));
        return ProductResponse.of(saved);
    }

    private Product toProduct(ProductRequest productRequest) {
        return new Product(productRequest.getName(), Price.valueOf(productRequest.getPrice()));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return ProductResponse.ofList(productRepository.findAll());
    }
}
