package kitchenpos.product.application;

import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductEntity;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductDao productDao;
    private final ProductRepository productRepository;

    public ProductService(ProductDao productDao, ProductRepository productRepository) {
        this.productDao = productDao;
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final Product product) {
        final BigDecimal price = product.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격이 없는 상품은 등록할 수 없습니다.");
        }

        return productDao.save(product);
    }

    @Transactional
    public ProductResponse createTemp(final ProductRequest productRequest) {

        ProductEntity productEntity =  productRepository.save(productRequest.toEntity());
        return ProductResponse.of(productEntity);
    }

    public List<Product> list() {
        return productDao.findAll();
    }

    public List<ProductResponse> listTemp() {
        List<ProductEntity> productEntities = productRepository.findAll();
        return productEntities.stream()
                .map(productEntity -> ProductResponse.of(productEntity))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductEntity findById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("등록되지 않은 상품입니다."));
    }

}
