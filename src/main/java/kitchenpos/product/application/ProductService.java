package kitchenpos.product.application;

import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product createdProduct = productRepository.save(new Product(new Name(productRequest.getName()), new Price(productRequest.getPrice())));
        return ProductResponse.of(createdProduct);
    }

    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(line -> ProductResponse.of(line))
                .collect(Collectors.toList());
    }
}
