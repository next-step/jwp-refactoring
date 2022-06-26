package kitchenpos.application.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductFixtureFactory {
    private MenuProductFixtureFactory() {
    }

    public static MenuProduct create(final Long seq, final Menu menu, final Product product, final long quantity) {
        return new MenuProduct(seq, menu, product, quantity);
    }

    public static MenuProduct createWithoutId( final Menu menu, final Product product, final long quantity) {
        return new MenuProduct(menu, product, quantity);
    }
}
