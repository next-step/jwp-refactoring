package kitchenpos.service.product;

import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.product.ProductResponse;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse save(final Product product) {
        return ProductResponse.of(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return ProductResponse.ofList(productRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Map<Long, Product> findProducts(List<Long> ids) {
        return productRepository.findByIdIn(ids).stream().collect(toMap(Product::getId, Function.identity()));
    }
}
