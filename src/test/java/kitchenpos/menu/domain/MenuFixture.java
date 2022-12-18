package kitchenpos.menu.domain;

import static java.util.Collections.singletonList;
import static kitchenpos.common.NameFixture.nameMenuA;
import static kitchenpos.common.PriceFixture.priceMenuA;
import static kitchenpos.menu.domain.MenuGroupFixture.menuGroup;
import static kitchenpos.menu.domain.MenuProductFixture.menuProductA;

public class MenuFixture {

    public static Menu menuA() {
        return new Menu(nameMenuA(), priceMenuA(), menuGroup(), singletonList(menuProductA()));
    }
}
