package kitchenpos.menu.domain.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;

import static java.util.Collections.singletonList;
import static kitchenpos.common.fixture.NameFixture.nameMenuA;
import static kitchenpos.common.fixture.PriceFixture.priceMenuA;
import static kitchenpos.menu.domain.fixture.MenuGroupFixture.menuGroupA;
import static kitchenpos.menu.domain.fixture.MenuProductFixture.menuProductA;

public class MenuFixture {

    public static Menu menuA(Long productId) {
        return new Menu(NameFixture.nameMenuA(), PriceFixture.priceMenuA(), menuGroupA(), new MenuProducts(Collections.singletonList(MenuProductFixture.menuProductA(productId))));
    }
}
