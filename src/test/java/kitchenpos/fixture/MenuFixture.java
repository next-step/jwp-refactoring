package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuGroupFixture.추천_메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.강정치킨_두마리;

public class MenuFixture {

    public static final Menu 강정치킨_두마리_셋트 = create(1L, "강정치킨_두마리_셋트", BigDecimal.valueOf(30_000), 추천_메뉴_그룹, Arrays.asList(강정치킨_두마리));

    private MenuFixture() {
        throw new UnsupportedOperationException();
    }

    public static Menu create(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(menuProducts);

        return menu;
    }
}
