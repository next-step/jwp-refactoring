package kitchenpos.menu;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

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

        return menu;
    }

}
