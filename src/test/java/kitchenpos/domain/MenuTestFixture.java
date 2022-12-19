package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class MenuTestFixture {

    public static Menu createMenu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu.of(id, name, price, menuGroupId, menuProducts);
    }

    public static MenuProduct createMenuProduct(Long seq, Long menuId, Long productId, Long quantity) {
        return MenuProduct.of(seq, menuId, productId, quantity);
    }

}
