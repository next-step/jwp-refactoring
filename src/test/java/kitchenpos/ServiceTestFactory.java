package kitchenpos;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ServiceTestFactory {
    public static Product HONEY_COMBO = createProductBy(1L, "허니콤보", 20_000L);
    public static Product RED_COMBO = createProductBy(2L,"레드콤보", 19_000L);
    public static MenuGroup NEW_MENU_GROUP = createMenuGroupBy(1L, "신메뉴");
    public static MenuGroup FAVORITE_MENU_GROUP = createMenuGroupBy(2L, "인기메뉴");
    public static Menu HONEY_RED_COMBO = createMenuBy(1L, "허니레드콤보", 39_000L);

    public static Product createProductBy(Long id, String name, long price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }

    public static MenuGroup createMenuGroupBy(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }

    public static Menu createMenuBy(Long id, String name, long price) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        return menu;
    }
}
