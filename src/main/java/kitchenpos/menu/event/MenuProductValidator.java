package kitchenpos.menu.event;

import kitchenpos.menu.domain.Menu;
import java.math.BigDecimal;

public class MenuProductValidator {

    public static void validPrice(Menu menu, BigDecimal menuProductSum) {
        menu.validSum(menuProductSum);
    }
}
