package kitchenpos.menu;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.*;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

import java.math.BigDecimal;
import java.util.List;

public class MenuFactory {
    public static MenuGroup ofMenuGroup(Long id, String name) {
        return MenuGroup.of(id, name);
    }

    public static MenuGroupRequest ofMenuGroupRequest(String name) {
        return MenuGroupRequest.of(name);
    }

    public static ProductRequest ofProductRequest(String name, int price) {
        return new ProductRequest(name, BigDecimal.valueOf(price));
    }

    public static ProductResponse ofProductResponse(long id, String name, int price) {
        return new ProductResponse(id, name, BigDecimal.valueOf(price));
    }

    public static MenuRequest ofMenuRequest(String name, BigDecimal price, Long productId, List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest(name, price, productId, menuProductRequests);
    }

    public static MenuProductRequest ofMenuProductRequest(Long productId, Long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public static Menu ofMenu(long id, String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        Menu menu = new Menu(id, name, price, menuGroup);
        menu.addMenuProducts(menuProducts);

        return menu;
    }

    public static Menu ofMenu(String name, Price price, MenuGroup menuGroup) {
        return new Menu(null, name, price, menuGroup);
    }

    public static Product ofProduct(long id, String name, int price) {
        return Product.of(id, name, Price.of(BigDecimal.valueOf(price)));
    }
}
