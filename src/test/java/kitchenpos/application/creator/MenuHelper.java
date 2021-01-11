package kitchenpos.application.creator;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-11
 */
public class MenuHelper {

    public static Menu create(String name, int price, MenuGroup menuGroup, MenuProduct...products) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(Arrays.asList(products));
        return menu;
    }
}
