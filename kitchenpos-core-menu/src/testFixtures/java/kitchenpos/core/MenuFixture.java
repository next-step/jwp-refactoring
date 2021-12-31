package kitchenpos.core;

import kitchenpos.core.domain.Menu;
import kitchenpos.core.domain.MenuProduct;
import kitchenpos.core.domain.Product;

import java.math.BigDecimal;
import java.util.List;

public class MenuFixture {
    private MenuFixture() {
    }

    public static MenuProduct getMenuProduct(Long id, Product product, int quantity) {
        return MenuProduct.generate(id, product.getId(), quantity);
    }


    public static Menu getMenu(long id, String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu.generate(id, name, menuProducts, menuGroupId, BigDecimal.valueOf(price));

    }
}
