package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menus.menu.domain.MenuProduct;
import kitchenpos.menus.menu.dto.MenuProductRequest;

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

    public static MenuProductRequest ofRequest(
        final Long productId,
        final long price,
        final long quantity
    ) {
        return new MenuProductRequest(productId, BigDecimal.valueOf(price), quantity);
    }
}
