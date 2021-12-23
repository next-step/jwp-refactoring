package kitchenpos.menu.product.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.menu.product.domain.Product;
import kitchenpos.menu.product.domain.ProductRepository;
import kitchenpos.menu.product.dto.ProductRequest;
import kitchenpos.menu.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product saveProduct = productRepository.save(productRequest.toEntity());
        return ProductResponse.of(saveProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return ProductResponse.ofList(productRepository.findAll());
    }
}
