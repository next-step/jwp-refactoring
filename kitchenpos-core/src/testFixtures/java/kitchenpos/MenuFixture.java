package kitchenpos;

import kitchenpos.domain.menu.domain.Menu;
import kitchenpos.domain.menu.domain.MenuProduct;
import kitchenpos.domain.product.Product;

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
