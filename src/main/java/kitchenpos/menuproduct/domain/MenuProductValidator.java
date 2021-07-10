package kitchenpos.menuproduct.domain;

import kitchenpos.menu.domain.Menu;

import java.math.BigDecimal;

public class MenuProductValidator {

    public static void validatePrice(MenuProducts menuProducts, Menu menu) {
        BigDecimal sum = menuProducts.sumOfMenuProducts();

        menu.compareMenuPriceToProductsSum(sum);
    }
}
