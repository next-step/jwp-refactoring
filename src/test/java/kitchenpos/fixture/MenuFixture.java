package kitchenpos.fixture;

import kitchenpos.domain.Menu;

import static kitchenpos.fixture.MenuGroupFixture.한마리메뉴_그룹;
import static kitchenpos.fixture.ProductFixture.후라이드치킨;
import static kitchenpos.fixture.ProductFixture.양념치킨;

public class MenuFixture {
    public static Menu menu1 = Menu.of(1L, 후라이드치킨.getName(), 후라이드치킨.getPrice(), 한마리메뉴_그룹.getId(), null);
    public static Menu menu2 = Menu.of(1L, 양념치킨.getName(), 양념치킨.getPrice(), 한마리메뉴_그룹.getId(), null);
}
