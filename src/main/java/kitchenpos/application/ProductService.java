package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.domain.exceptions.product.InvalidProductPriceException;
import kitchenpos.ui.dto.product.ProductRequest;
import kitchenpos.ui.dto.product.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        final BigDecimal price = productRequest.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidProductPriceException("상품의 가격은 반드시 있어야 하며, 0원 이상이어야 합니다.");
        }

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        Product saved = productDao.save(product);

        return ProductResponse.of(saved);
    }

    public List<ProductResponse> list() {
        List<Product> products = productDao.findAll();

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
