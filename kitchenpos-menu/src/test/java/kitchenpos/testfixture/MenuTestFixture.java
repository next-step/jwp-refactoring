package kitchenpos.testfixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.*;

import java.math.BigDecimal;
import java.util.List;

public class MenuTestFixture {
    public static Product createProduct(String name, BigDecimal price) {
        return new Product(name, price);
    }

    public static MenuGroup createMenuGroup(String name) {
        return new MenuGroup(name);
    }

    public static MenuGroupRequest createMenuGroupRequest(String name) {
        return new MenuGroupRequest(name);
    }

    public static Menu createMenu(Long menuGroupId, String name, BigDecimal price, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public static Menu createMenu(Long id, Long menuGroupId, String name, BigDecimal price, List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroupId, menuProducts);
    }

    public static MenuRequest createMenuRequest(Long menuGroupId, String name, BigDecimal price, List<MenuProductRequest> menuProducts) {
        return new MenuRequest(name, price, menuGroupId, menuProducts);
    }

    public static MenuProduct createMenuProduct(Long productId, int quantity) {
        return new MenuProduct(productId, (long) quantity);
    }

    public static MenuProductRequest createMenuProductRequest(Long productId, int quantity) {
        return new MenuProductRequest(productId, (long) quantity);
    }
}
