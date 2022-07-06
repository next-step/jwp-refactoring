package kitchenpos.menu.fixture;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;

import java.math.BigDecimal;
import java.util.Arrays;

import static kitchenpos.menu.fixture.MenuProductFixture.메뉴_양념_치킨;
import static kitchenpos.menu.fixture.MenuProductFixture.메뉴_치킨;
import static kitchenpos.menugroup.fixture.MenuGroupFixture.추천_메뉴;

public class MenuFixture {

    public static Menu 기본_메뉴 = create(
            1L,
            Name.of("기본 메뉴"),
            Price.of(BigDecimal.valueOf(17_000)),
            추천_메뉴.getId(),
            MenuProducts.of(Arrays.asList(메뉴_치킨))
    );

    public static Menu 추천_기본_메뉴 = create(
            2L,
            Name.of("추천 기본 메뉴"),
            Price.of(BigDecimal.valueOf(17_000)),
            추천_메뉴.getId(),
            MenuProducts.of(Arrays.asList(메뉴_양념_치킨))
    );

    public static Menu create(Long id, Name name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        return Menu.of(id, name, price, menuGroupId, menuProducts);
    }
}
