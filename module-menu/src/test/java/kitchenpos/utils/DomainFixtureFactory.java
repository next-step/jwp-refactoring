package kitchenpos.utils;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;

public class DomainFixtureFactory {
    public static Product createProduct(Long id, String name, BigDecimal price) {
        return Product.from(id, name, price);
    }

    public static ProductRequest createProductRequest(String name, BigDecimal price) {
        return new ProductRequest(name, price);
    }

    public static MenuGroup createMenuGroup(Long id, String name) {
        return MenuGroup.from(id, name);
    }

    public static MenuGroupRequest createMenuGroupRequest(String name) {
        return new MenuGroupRequest(name);
    }

    public static Menu createMenu(String name, BigDecimal price, Long menuGroupId, MenuProducts menuProducts) {
        return Menu.from(name, price, menuGroupId, menuProducts);
    }

    public static Menu createMenu(Long id, String name, BigDecimal price, Long menuGroupId, MenuProducts menuProducts) {
        return Menu.from(id, name, price, menuGroupId, menuProducts);
    }

    public static MenuRequest createMenuRequest(String name, BigDecimal price, long menuGroupId,
                                                List<MenuProductRequest> menuProducts) {
        return new MenuRequest(name, price, menuGroupId, menuProducts);
    }

    public static MenuProduct createMenuProduct(Long productId, long quantity) {
        return MenuProduct.from(productId, quantity);
    }
}
