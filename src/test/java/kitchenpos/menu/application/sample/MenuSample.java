package kitchenpos.menu.application.sample;

import static kitchenpos.menu.application.sample.MenuGroupSample.두마리메뉴;
import static kitchenpos.menu.application.sample.MenuProductSample.후라이드치킨두마리;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;

public class MenuSample {

    public static Menu 후라이드치킨세트() {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName(Name.from("후라이드치킨세트"));
        menu.setPrice(Price.from(BigDecimal.TEN));
        menu.setMenuProducts(MenuProducts.singleton(후라이드치킨두마리()));
        menu.setMenuGroup(두마리메뉴());
        return menu;
    }
}
