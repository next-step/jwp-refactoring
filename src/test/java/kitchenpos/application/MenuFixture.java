package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

    private MenuFixture() {
    }

    public static Menu ofCreateRequest(
        final String name,
        final BigDecimal price,
        final Long menuGroupId,
        final List<MenuProduct> menuProducts
    ) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static Menu of(
        final Long id,
        final String name,
        final BigDecimal price,
        final Long menuGroupId,
        final List<MenuProduct> menuProducts
    ) {
        final Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static MenuProduct ofMenuProduct(
        final Long seq,
        final Long productId,
        final Long quantity
    ) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
