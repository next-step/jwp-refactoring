package kitchenpos.menu.event;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;

import java.math.BigDecimal;

public class MenuProductValidator {

    public static void validPrice(MenuProducts menuProducts, Menu menu) {
        BigDecimal sum = menuProducts.calculateSum();

        menu.validSum(sum);
    }
}
