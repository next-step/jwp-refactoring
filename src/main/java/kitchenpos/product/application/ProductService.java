package kitchenpos.product.application;

import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ProductService {

    private static final String ERROR_MESSAGE_NOT_EXIST_PRODUCT = "상품 정보가 존재하지 않습니다.";
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product product = productRepository.save(productRequest.toProduct());
        return ProductResponse.from(product);
    }

    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();
        return ProductResponse.fromList(products);
    }

    public Product findProduct(long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException(ERROR_MESSAGE_NOT_EXIST_PRODUCT));
    }
}
