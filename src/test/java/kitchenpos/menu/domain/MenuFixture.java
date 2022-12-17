package kitchenpos.menu.domain;

import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.Collections;

import static kitchenpos.menu.domain.MenuGroupFixture.menuGroup;

public class MenuFixture {

    public static final String MENU_A = "메뉴 A";

    public static Menu menu() {
        return new Menu(new Name(MENU_A), new Price(BigDecimal.ONE), menuGroup(), Collections.singletonList(new MenuProduct(null, new Product(new Name("상품"), new Price(BigDecimal.ONE)), 1L)));
    }
}
