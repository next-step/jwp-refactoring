package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductDao;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        final Product product = new Product(productRequest.getName(), productRequest.getPrice());
        final Product saved = productDao.save(product);

        return ProductResponse.of(saved);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productDao.findAll()
            .stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }
}
