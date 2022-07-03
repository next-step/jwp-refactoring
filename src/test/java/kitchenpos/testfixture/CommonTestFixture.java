package kitchenpos.testfixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.order.domain.*;
import kitchenpos.product.domain.*;
import kitchenpos.product.dto.ProductRequest;

import java.math.BigDecimal;
import java.util.List;

public class CommonTestFixture {
    public static Product createProduct(String name, BigDecimal price) {
        return new Product(name, price);
    }

    public static ProductRequest createProductRequest(String name, BigDecimal price) {
        return new ProductRequest(name, price);
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

    public static MenuProduct createMenuProduct(Product product, int quantity) {
        return new MenuProduct(product, (long) quantity);
    }

    public static MenuProductRequest createMenuProductRequest(Long productId, int quantity) {
        return new MenuProductRequest(productId, (long) quantity);
    }

    public static Order createOrder(Long id, Long orderTableId, String orderStatus) {
        return new Order(id, orderTableId, orderStatus);
    }

    public static Order createOrder(Long id, Long orderTableId, String orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderLineItems);
    }

    public static OrderLineItem createOrderLineItem(int seq, Long orderId, Long menuId, long quantity) {
        return new OrderLineItem((long) seq, orderId, menuId, quantity);
    }

    public static OrderTable createOrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public static TableGroup createTableGroup(Long id, List<OrderTable> orderTables) {
        return new TableGroup(id, orderTables);
    }
}
