package kitchenpos.__fixture__;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuTestFixture {
    public static Menu 메뉴_생성(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                             final MenuProduct... menuProducts) {
        return new Menu(id, name, price, menuGroupId, menuProducts);
    }
}
