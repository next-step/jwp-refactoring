package kitchenpos.testfixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.TableGroupRequest;
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

    public static Order createOrder(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderLineItems);
    }

    public static Order createOrder(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderStatus, orderLineItems);
    }

    public static OrderRequest createOrderRequest(Long orderTableId, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, orderStatus, orderLineItems);
    }

    public static OrderLineItemRequest createOrderLineItemRequest(Long menuId, int quantity) {
        return new OrderLineItemRequest(menuId, (long) quantity);
    }

    public static OrderLineItem createOrderLineItem(Long menuId, long quantity) {
        return new OrderLineItem(menuId, quantity);
    }

    public static OrderTableRequest createOrderTableRequest(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }

    public static OrderTable createOrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }

    public static TableGroup createTableGroup(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }

    public static TableGroupRequest createTableGroupRequest(List<Long> orderTablesIds) {
        return new TableGroupRequest(orderTablesIds);
    }
}
