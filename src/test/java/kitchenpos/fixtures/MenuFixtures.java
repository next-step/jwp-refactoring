package kitchenpos.fixtures;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;

/**
 * packageName : kitchenpos.fixtures
 * fileName : MenuFixtures
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
public class MenuFixtures {

    public static Menu createMenu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }
}
