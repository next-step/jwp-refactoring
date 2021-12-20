package kitchenpos.api.application.product;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.domain.product.Product;
import kitchenpos.common.domain.product.ProductRepository;
import kitchenpos.common.dto.product.ProductRequest;
import kitchenpos.common.dto.product.ProductResponse;
import kitchenpos.common.utils.StreamUtils;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        return ProductResponse.from(productRepository.save(productRequest.toProduct()));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();
        return StreamUtils.mapToList(products, ProductResponse::from);
    }
}
