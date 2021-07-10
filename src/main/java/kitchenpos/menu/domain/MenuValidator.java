package kitchenpos.menu.domain;

import kitchenpos.menuproduct.domain.MenuProducts;

import java.math.BigDecimal;

public class MenuValidator {

    public static void validatePrice(MenuProducts menuProducts, Menu menu) {
        BigDecimal sum = menuProducts.sumOfMenuProducts();

        menu.compareMenuPriceToProductsSum(sum);
    }
}
