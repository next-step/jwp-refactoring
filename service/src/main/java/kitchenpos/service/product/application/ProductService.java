package kitchenpos.service.product.application;

import kitchenpos.domain.product.ProductRepository;
import kitchenpos.service.product.dto.ProductRequest;
import kitchenpos.service.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productDao) {
        this.productRepository = productDao;
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        return new ProductResponse(productRepository.save(request.toProduct()));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productRepository.findAll().stream().map(ProductResponse::new).collect(Collectors.toList());
    }
}
