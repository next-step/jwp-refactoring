package kitchenpos.fixture;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupName;
import kitchenpos.domain.MenuName;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.NumberOfGuests;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductName;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.TableGroup;

public class DomainFactory {
    public static Product createProduct(Long id, String name, long price) {
        Product product = new Product(id, ProductName.from(name), Price.from(price));

        return product;
    }

    public static Product createEmptyPriceProduct(Long id, String name) {
        Product product = new Product(id, ProductName.from(name), null);

        return product;
    }

    public static Menu createMenu(Long id, String name, double price, Long menuGroupId,
                                  List<MenuProduct> menuProducts) {
        return new Menu(id, MenuName.from(name), Price.from(price), menuGroupId, MenuProducts.from(menuProducts));
    }

    public static Menu createEmptyPriceMenu(Long id, String name, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(id, MenuName.from(name), null, menuGroupId, MenuProducts.from(menuProducts));
    }

    public static MenuProduct createMenuProduct(Long seq, Menu menu, Product product, long quantity) {
        MenuProduct menuProduct = new MenuProduct(seq, menu, product, Quantity.from(quantity));

        return menuProduct;
    }

    public static MenuGroup createMenuGroup(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup(id, MenuGroupName.from(name));
        return menuGroup;
    }

    public static OrderTable createOrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable(id, tableGroupId, NumberOfGuests.from(numberOfGuests), empty);
        return orderTable;
    }

    public static Order createOrder(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime,
                                    List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderedTime(orderedTime);
        order.setOrderLineItems(orderLineItems);

        return order;
    }

    public static OrderLineItem createOrderLineItem(Long seq, Long orderId, Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);

        return orderLineItem;
    }

    public static TableGroup createTableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup(id, createdDate, orderTables);
        return tableGroup;
    }
}
