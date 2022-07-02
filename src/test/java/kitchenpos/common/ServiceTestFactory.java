package kitchenpos.common;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.product.domain.Price;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.tablegroup.domain.TableGroup;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ServiceTestFactory {
    public static OrderTable 테이블생성(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }


    public static Menu 메뉴생성(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName("오늘의메뉴");
        menu.setPrice(Price.from(price));
        menu.setMenuGroup(menuGroup);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static MenuProduct 메뉴상품생성(Long seq, Product product, Long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setProduct(product);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static MenuGroup 메뉴그룹생성(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }

    public static Product 상품생성(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static TableGroup 테이블그룹생성(Long id, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }

    public static Order 주문생성(Long id, Long orderTableId, String orderStatus, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static OrderLineItem 주문항목생성(Long seq, Long orderId, Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
