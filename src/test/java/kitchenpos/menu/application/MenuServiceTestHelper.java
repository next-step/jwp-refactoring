package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.Arrays;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuServiceTestHelper {
    public static Menu 메뉴_정보(String name, int price, Long menuGroupId, MenuProduct... menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(Arrays.asList(menuProducts));
        return menu;
    }

    public static MenuProduct 메뉴_상품_정보(Long productId, int quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
