package kitchenpos.menu;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;

public class MenuTestSupport {

    /**
     * 새로운 메뉴를 만듭니다.
     * @param name
     * @return 상품
     */
    public static Menu createMenu(String name, long price, Long menuGroupId) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(new ArrayList<>());

        return menu;
    }

    /**
     * 메뉴에 새로운 메뉴상품을 추가합니다.
     * @return 메뉴상품
     */
    public static Menu addMenuGroup(Menu menu, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        ReflectionTestUtils.setField(menuProduct, "menuId", menu.getId());
        ReflectionTestUtils.setField(menuProduct, "productId", productId);
        ReflectionTestUtils.setField(menuProduct, "quantity", quantity);

        menu.getMenuProducts().add(menuProduct);
        return menu;
    }
}
