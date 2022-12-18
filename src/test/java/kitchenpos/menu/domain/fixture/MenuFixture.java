package kitchenpos.menu.domain.fixture;

import kitchenpos.menu.domain.Menu;

import static java.util.Collections.singletonList;
import static kitchenpos.common.fixture.NameFixture.nameMenuA;
import static kitchenpos.common.fixture.PriceFixture.priceMenuA;
import static kitchenpos.menu.domain.fixture.MenuGroupFixture.menuGroupA;
import static kitchenpos.menu.domain.fixture.MenuProductFixture.menuProductA;

public class MenuFixture {

    public static Menu menuA() {
        return new Menu(nameMenuA(), priceMenuA(), menuGroupA(), singletonList(menuProductA()));
    }
}
