package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductDao;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private static final String ERROR_MESSAGE_NOT_EXIST_PRODUCT = "상품 정보가 존재하지 않습니다.";
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product product = productDao.save(productRequest.toProduct());
        return ProductResponse.from(product);
    }

    public List<ProductResponse> list() {
        return productDao.findAll()
            .stream().map(ProductResponse::from)
            .collect(Collectors.toList());
    }

    public Product findProduct(long productId) {
        return productDao.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException(ERROR_MESSAGE_NOT_EXIST_PRODUCT));
    }
}
