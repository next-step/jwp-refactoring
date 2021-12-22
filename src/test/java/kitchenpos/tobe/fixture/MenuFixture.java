package kitchenpos.tobe.fixture;

import java.math.BigDecimal;
import kitchenpos.tobe.common.domain.Name;
import kitchenpos.tobe.common.domain.Price;
import kitchenpos.tobe.menu.domain.Menu;
import kitchenpos.tobe.menu.domain.MenuProducts;
import kitchenpos.tobe.menu.domain.MenuValidator;

public class MenuFixture {

    private MenuFixture() {
    }

    public static Menu of(
        final Long id,
        final String name,
        final long price,
        final MenuProducts menuProducts,
        final Long menuGroupId,
        final MenuValidator menuValidator
    ) {
        return new Menu(
            id,
            new Name(name),
            new Price(BigDecimal.valueOf(price)),
            menuProducts,
            menuGroupId,
            menuValidator
        );
    }

    public static Menu of(
        final String name,
        final long price,
        final MenuProducts menuProducts,
        final Long menuGroupId,
        final MenuValidator menuValidator
    ) {
        return of(null, name, price, menuProducts, menuGroupId, menuValidator);
    }
}
