package kitchenpos.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.product.domain.Product;

public class DomainFixtureFactory {
    public static Product createProduct(Long id, String name, BigDecimal price) {
        return Product.of(id, name, price);
    }

    public static MenuGroup createMenuGroup(Long id, String name) {
        return MenuGroup.of(id, name);
    }

    public static Menu createMenu(Long id, String name, BigDecimal price, MenuGroup menuGroup,
                                  List<MenuProduct> menuProducts) {
        return Menu.of(id, name, price, menuGroup, menuProducts);
    }

    public static MenuProduct createMenuProduct(Long seq, Menu menu, Product product, long quantity) {
        return MenuProduct.of(seq, menu, product, quantity);
    }

    public static OrderTable createOrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return OrderTable.of(id, tableGroup, numberOfGuests, empty);
    }

    public static Order createOrder(Long id, OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        return Order.of(id, orderTable, orderStatus, orderLineItems);
    }

    public static OrderLineItem createOrderLineItem(Long seq, Order order, Menu menu, long quantity) {
        return OrderLineItem.of(seq, order, menu, quantity);
    }

    public static TableGroup createTableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        return TableGroup.of(id, createdDate, orderTables);
    }
}
