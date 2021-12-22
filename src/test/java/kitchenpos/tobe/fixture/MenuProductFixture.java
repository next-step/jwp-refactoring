package kitchenpos.tobe.fixture;

import java.math.BigDecimal;
import kitchenpos.tobe.common.domain.Price;
import kitchenpos.tobe.common.domain.Quantity;
import kitchenpos.tobe.menu.domain.MenuProduct;

public class MenuProductFixture {

    private MenuProductFixture() {
    }

    public static MenuProduct of(
        final Long seq,
        final Long productId,
        final long price,
        final long quantity
    ) {
        return new MenuProduct(
            seq,
            productId,
            new Price(BigDecimal.valueOf(price)),
            new Quantity(quantity)
        );
    }

    public static MenuProduct of(
        final Long productId,
        final long price,
        final long quantity
    ) {
        return of(null, productId, price, quantity);
    }
}
