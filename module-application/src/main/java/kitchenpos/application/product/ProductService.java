package kitchenpos.application.product;

import kitchenpos.domain.menu.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.product.dto.ProductRequest;
import kitchenpos.domain.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        final Product newProduct = Product.of(productRequest.getName(), Price.of(new BigDecimal(productRequest.getPrice())));

        return new ProductResponse(productRepository.save(newProduct));
    }

    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
            .map(ProductResponse::new)
            .collect(toList());
    }
}
