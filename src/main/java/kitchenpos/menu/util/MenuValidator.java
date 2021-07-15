package kitchenpos.menu.util;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.exception.NotFoundProductException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MenuValidator {
    private final ProductRepository productRepository;

    private MenuValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validateToMenu(Menu menu) {
        if (!menu.getPrice().isCheapThanProductsPrice(calculateAllProductsPrice(menu.getMenuProducts()))) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal calculateAllProductsPrice(List<MenuProduct> menuProducts) {
        BigDecimal productsPrice = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            productsPrice = productsPrice.add(getPriceByQuantity(menuProduct.getProductId(), menuProduct.getQuantity()));
        }
        return productsPrice;
    }

    private BigDecimal getPriceByQuantity(Long productId, Long quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundProductException());
        return product.getPriceByQuantity(quantity);

    }
}
