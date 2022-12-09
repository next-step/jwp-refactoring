package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹_기본;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품_후라이드_치킨;

public class MenuFixture {

    public static Menu 메뉴_기본 = MenuFixture.create(1L, BigDecimal.valueOf(15_000), 메뉴_그룹_기본.getId(), Arrays.asList(메뉴_상품_후라이드_치킨));

    public static Menu create(Long id, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

}
