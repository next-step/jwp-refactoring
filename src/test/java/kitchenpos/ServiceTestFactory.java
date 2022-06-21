package kitchenpos;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ServiceTestFactory {
    public static Product HONEY_COMBO = createProductBy(1L, "허니콤보", 20_000L);
    public static Product RED_COMBO = createProductBy(2L,"레드콤보", 19_000L);
    public static Product ORIGINAL_COMBO = createProductBy(3L,"오리지널콤보", 18_000L);
    public static MenuGroup NEW_MENU_GROUP = createMenuGroupBy(1L, "신메뉴");
    public static MenuGroup FAVORITE_MENU_GROUP = createMenuGroupBy(2L, "인기메뉴");
    public static Menu HONEY_RED_COMBO = createMenuBy(1L, "허니레드콤보", 39_000L);
    public static MenuProduct HONEY_MENU_PRODUCT = createMenuProductBy(1L, HONEY_RED_COMBO, HONEY_COMBO, 1);
    public static MenuProduct NOT_EXISTS_MENU_PRODUCT = createMenuProductBy(2L, HONEY_RED_COMBO, ORIGINAL_COMBO, 1);
    public static MenuProduct RED_MENU_PRODUCT = createMenuProductBy(3L, HONEY_RED_COMBO, RED_COMBO, 1);

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

    public static MenuProduct createMenuProductBy(Long seq, Menu menu, Product product, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menu.getId());
        menuProduct.setQuantity(quantity);
        menuProduct.setProductId(product.getId());
        return menuProduct;
    }
}
