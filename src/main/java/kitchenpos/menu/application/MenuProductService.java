package kitchenpos.menu.application;

import kitchenpos.common.exception.IllegalArgumentException;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MenuProductService {
    private final ProductRepository productRepository;

    public MenuProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public BigDecimal calculateTotalPrice(MenuProducts menuProducts) {
        return menuProducts.asList()
                .stream()
                .map(it -> it.multiplyByQuantity(findProductById(it.getProductId()).getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
    }
}
