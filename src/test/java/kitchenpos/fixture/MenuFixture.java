package kitchenpos.fixture;

import kitchenpos.domain.Menu;

import static kitchenpos.fixture.MenuGroupFixture.menuGroup2;
import static kitchenpos.fixture.ProductFixture.product1;
import static kitchenpos.fixture.ProductFixture.product2;

public class MenuFixture {
    public static Menu menu1 = Menu.of(1L, product1.getName(), product1.getPrice(), menuGroup2.getId(), null);
    public static Menu menu2 = Menu.of(1L, product2.getName(), product2.getPrice(), menuGroup2.getId(), null);
}
