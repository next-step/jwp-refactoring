package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuGroupFixture.추천_메뉴;
import static kitchenpos.fixture.MenuProductFixture.메뉴_양념_치킨;
import static kitchenpos.fixture.MenuProductFixture.메뉴_치킨;

public class MenuFixture {

    public static Menu 기본_메뉴 = create(1L, BigDecimal.valueOf(17_000), 추천_메뉴.getId(), Arrays.asList(메뉴_치킨));
    public static Menu 추천_기본_메뉴 = create(2L, BigDecimal.valueOf(17_000), 추천_메뉴.getId(), Arrays.asList(메뉴_양념_치킨));

    public static Menu create(Long id, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

}
