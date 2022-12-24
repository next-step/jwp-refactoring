package kitchenpos.menu.domain.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Name;
import kitchenpos.menu.domain.Price;

import java.math.BigDecimal;

import static kitchenpos.menu.domain.fixture.MenuGroupFixture.menuGroupA;
import static kitchenpos.menu.domain.fixture.MenuProductsFixture.menuProducts;

public class MenuFixture {

    public static Menu menuA(Long productId) {
        return new Menu(1L, new Name("a"), new Price(BigDecimal.ONE), menuGroupA(), new MenuProducts(menuProducts(1L)));
    }
}
