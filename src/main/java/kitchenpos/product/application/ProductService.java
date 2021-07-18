package kitchenpos.product.application;

import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Product;
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

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(final ProductRequest productRequest) {
        Product saved = productRepository.save(toProduct(productRequest));
        return toProductResponse(saved);
    }

    private ProductResponse toProductResponse(Product product) {
       return new ProductResponse(product.getId(), product.getName(), product.getPrice().value());
    }

    private Product toProduct(ProductRequest productRequest) {
        return new Product(productRequest.getName(), Price.valueOf(productRequest.getPrice()));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return toProductResponses(productRepository.findAll());
    }

    private List<ProductResponse> toProductResponses(List<Product> products) {
        return products.stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }
}
