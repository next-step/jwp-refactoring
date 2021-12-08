package kitchenpos.application.sample;

import static kitchenpos.application.sample.MenuGroupSample.면류;
import static kitchenpos.application.sample.MenuProductSample.짜장면_두개;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.domain.Menu;

public class MenuSample {

    public static Menu 짜장면_세트() {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("짜장면 세트");
        menu.setPrice(BigDecimal.TEN);
        menu.setMenuProducts(Collections.singletonList(짜장면_두개()));
        menu.setMenuGroupId(면류().getId());
        return menu;
    }

}
