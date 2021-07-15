package kitchenpos.product.application;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import kitchenpos.menu.application.ProductMenuService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.ProductNotFoundException;

@Service
public class ProductMenuServiceImpl implements ProductMenuService {
    private final ProductRepository productRepository;

    public ProductMenuServiceImpl(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public BigDecimal calculateProductsPrice(final Long productId, final Long productCount) {
        Product product = this.productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        return product.getProductPrice().multiply(BigDecimal.valueOf(productCount));
    }
}
