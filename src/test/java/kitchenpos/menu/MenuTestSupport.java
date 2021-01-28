package kitchenpos.menu;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

public class MenuTestSupport {

    /**
     * 새로운 메뉴를 만듭니다.
     * @param name
     * @param menuGroup
     * @return 상품
     */
    public static Menu createMenu(String name, long price, MenuGroup menuGroup) {
        Menu menu = new Menu();
        menu.changeName(name);
        menu.changePrice(BigDecimal.valueOf(price));
        menu.setMenuGroup(menuGroup);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenu(new Menu());
        menuProduct.setProduct(new Product());
        menuProduct.setQuantity(3);
        menu.addMenuProducts(menuProduct);

        return menu;
    }

}
