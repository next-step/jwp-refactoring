package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductDao;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        Product product = request.toProduct();

        Product persistProduct = productDao.save(product);
        return ProductResponse.of(persistProduct);
    }

    public List<ProductResponse> list() {
        List<Product> products = productDao.findAll();

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public Product findById(Long productId) {
        return productDao.findById(productId)
                .orElseThrow(NoSuchElementException::new);
    }
}
