package kitchenpos.menu.application;

import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.MenuGroupRequiredException;
import kitchenpos.common.exception.MenuProductSumPriceException;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class MenuValidator {
    private final ProductService productService;

    public MenuValidator(ProductService productService) {
        this.productService = productService;
    }

    public void validateMenu(MenuGroup menuGroup, MenuRequest menuRequest) {
        Optional.ofNullable(menuGroup)
                .orElseThrow(MenuGroupRequiredException::new);

        if (!isPossibleMenu(menuRequest)) {
            throw new MenuProductSumPriceException(menuRequest.getPrice());
        }
    }

    public boolean isPossibleMenu(MenuRequest menu) {
        List<MenuProduct> menuProducts = menu.getMenuProducts();
        BigDecimal price = menu.getPrice();
        BigDecimal sum = menuProducts.stream()
                .map(menuProduct -> {
                    Product product = productService.findById(menuProduct.getProductId());
                    return multiply(product.getPrice(), menuProduct.getQuantity());
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return price.compareTo(sum) > Price.MINIMUM_PRICE_NUMBER;
    }

    public BigDecimal multiply(BigDecimal price, Long quantity) {
        return BigDecimal.valueOf(quantity)
                .multiply(price);
    }
}
