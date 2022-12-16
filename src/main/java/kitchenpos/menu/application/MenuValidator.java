package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

@Component
public class MenuValidator {
    public void validate(Menu menu, List<Product> products, boolean menuGroupNotExists) {
        validatePriceNotNull(menu.getPrice());
        validatePriceGreaterThanZero(menu.getPrice());
        validateMenuGroupExists(menuGroupNotExists);
        validateMenuProductSum(menu, products);
    }

    private void validatePriceNotNull(BigDecimal price) {
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException("메뉴의 가격이 null일 수 없습니다.");
        }
    }

    private void validatePriceGreaterThanZero(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 1) {
            throw new IllegalArgumentException("메뉴의 가격이 0이하일 수 없습니다.");
        }
    }

    private void validateMenuGroupExists(boolean menuGroupNotExists) {
        if (menuGroupNotExists) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }
    }

    private void validateMenuProductSum(Menu menu, List<Product> products) {
        BigDecimal sum = menuProductSum(menu.getMenuProducts(), products);
        if (menu.getPrice().compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴 가격은 상품 가격의 합계보다 비쌀 수 없습니다.");
        }
    }

    private BigDecimal menuProductSum(List<MenuProduct> menuProducts, List<Product> products) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            BigDecimal price = products.stream()
                .filter(product -> product.hasId(menuProduct.getProductId()))
                .map(Product::getPrice)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 ID 입니다."));

            sum = sum.add(price.multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return sum;
    }
}
