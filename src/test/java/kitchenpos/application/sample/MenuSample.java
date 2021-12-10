package kitchenpos.application.sample;

import static kitchenpos.application.sample.MenuGroupSample.두마리메뉴;
import static kitchenpos.application.sample.MenuProductSample.후라이드치킨두마리;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.domain.Menu;

public class MenuSample {

    public static Menu 후라이드치킨세트() {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("후라이드치킨세트");
        menu.setPrice(BigDecimal.TEN);
        menu.setMenuProducts(Collections.singletonList(후라이드치킨두마리()));
        menu.setMenuGroupId(두마리메뉴().getId());
        return menu;
    }
}
