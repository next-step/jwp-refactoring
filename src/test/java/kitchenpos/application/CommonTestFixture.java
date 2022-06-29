package kitchenpos.application;

import kitchenpos.domain.*;

import java.math.BigDecimal;
import java.util.List;

public class CommonTestFixture {
    public static Product createProduct(Long id, String name, int price) {
        return new Product(id, name, BigDecimal.valueOf(price));
    }

    public static MenuGroup createMenuGroup(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public static Menu createMenu(Long id, Long menuGroupId, String name, int price, List<MenuProduct> menuProducts) {
        return new Menu(id, name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
    }

    public static MenuProduct createMenuProduct(int seq, Long menuId, Long productId, int quantity) {
        return new MenuProduct((long) seq, menuId, productId, (long) quantity);
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
}
