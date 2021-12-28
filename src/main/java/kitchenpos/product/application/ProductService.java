package kitchenpos.product.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.dto.ProductResponses;
import kitchenpos.product.domain.ProductRepository;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        return ProductResponse.from(productRepository.save(request.toEntity()));
    }

    public ProductResponses list() {
        List<Product> products = productRepository.findAll();
        return ProductResponses.from(products);
    }
}
