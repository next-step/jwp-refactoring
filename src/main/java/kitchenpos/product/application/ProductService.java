package kitchenpos.product.application;

import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest product) {
        Price.of(product.getPrice());

        Product savedProduct = productRepository.save(product.toProduct());
        return ProductResponse.of(savedProduct);
    }

    public List<ProductResponse> findAll() {
        return ProductResponse.ofList(productRepository.findAll());
    }
}