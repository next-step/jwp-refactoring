package kitchenpos.menu.application;

import java.util.stream.Collectors;
import kitchenpos.menu.dao.ProductDao;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductEntity;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.domain.request.ProductRequest;
import kitchenpos.menu.domain.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductDao productDao;
    private final ProductRepository productRepository;

    public ProductService(final ProductDao productDao, ProductRepository productRepository) {
        this.productDao = productDao;
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final Product product) {
        final BigDecimal price = product.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        return productDao.save(product);
    }

    public List<Product> list() {
        return productDao.findAll();
    }

    @Transactional
    public ProductResponse createCopy(final ProductRequest productRequest) {
        ProductEntity product = ProductEntity.of(productRequest.getName(), productRequest.getPrice());
        product = productRepository.save(product);
        return ProductResponse.of(product);
    }

    public List<ProductResponse> listCopy() {
        List<ProductEntity> products = productRepository.findAll();
        return products.stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }
}
