package kitchenpos.utils;

import kitchenpos.domain.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestHelper {
    public static final MenuGroup 두마리_메뉴그룹 = menuGroup_생성(1L, "두마리메뉴");
    public static final Product 후라이드 = product_생성(1L, "후라이드", BigDecimal.valueOf(16000));
    public static final Product 양념치킨 = product_생성(2L, "양념치킨", BigDecimal.valueOf(16000));
    public static final MenuProduct 후라이드_1개 = menuProduct_생성(후라이드.getId(), 1);
    public static final MenuProduct 양념치킨_1개 = menuProduct_생성(양념치킨.getId(), 1);
    public static final Menu menu = menu_생성(1L, "후라이드양념치킨", BigDecimal.valueOf(16000), 두마리_메뉴그룹.getId());

    public static final TableGroup init_tableGroup = tableGroup_생성(1L);
    public static final OrderTable empty_orderTable1 = 빈_orderTable_생성(1L);
    public static final OrderTable empty_orderTable2 = 빈_orderTable_생성(2L);
    public static final OrderTable not_empty_orderTable = orderTable_생성(empty_orderTable1.getId(), false);
    public static final TableGroup tableGroup = tableGroup_orderTables_추가(init_tableGroup, Arrays.asList(empty_orderTable1, empty_orderTable2));
    public static final OrderTable orderTable1 = orderTable_groupId_추가(empty_orderTable1, tableGroup.getId(), false);
    public static final OrderTable orderTable2 = orderTable_groupId_추가(empty_orderTable2, tableGroup.getId(), false);
    public static final List<OrderTable> 그룹으로_묶여있는_orderTables = Arrays.asList(orderTable1, orderTable2);

    public static final OrderLineItem orderLineItem = orderLineItem_생성(1L, menu.getId(), 2);
    public static final Order order = order_생성(1L, Collections.singletonList(orderLineItem));
    public static final Order 주문항목이_없는_order = order_생성(1L, null);
    public static final Order 요리중_order = order_status_추가(order, OrderStatus.COOKING.name());
    public static final Order 식사_order = order_status_추가(order, OrderStatus.MEAL.name());
    public static final Order 완료된_order = order_status_추가(order, OrderStatus.COMPLETION.name());

    public static MenuGroup menuGroup_생성(Long id, String name) {
        return MenuGroup.of(id, name);
    }

    public static Menu menu_생성(Long id, String name, BigDecimal price, Long menuGroupId) {
        return Menu.of(id, name, price, menuGroupId);
    }

    public static Menu menu_price_변경(Menu menu, BigDecimal price) {
        return menu_생성(menu.getId(), menu.getName(), price, menu.getMenuGroupId());
    }

    public static Menu menu_menuGroupId_변경(Menu menu, Long menuGroupId) {
        return menu_생성(menu.getId(), menu.getName(), menu.getPrice(), menuGroupId);
    }

    public static Product product_생성(Long id, String name, BigDecimal price) {
        return Product.of(id, name, price);
    }

    public static Product product_price_변경(Product product, BigDecimal price) {
        return product_생성(product.getId(), product.getName(), price);
    }

    public static MenuProduct menuProduct_생성(Long productId, long quantity) {
        return MenuProduct.of(productId, quantity);
    }

    public static TableGroup tableGroup_생성(Long id) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        return tableGroup;
    }

    public static TableGroup tableGroup_orderTables_추가(TableGroup tableGroup, List<OrderTable> orderTables) {
        TableGroup newTableGroup = tableGroup_생성(tableGroup.getId());
        newTableGroup.setOrderTables(orderTables);
        return newTableGroup;
    }

    public static OrderTable orderTable_생성(Long id, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable 빈_orderTable_생성(Long id) {
        return orderTable_생성(id, true);
    }

    public static OrderTable orderTable_groupId_추가(OrderTable orderTable, Long tableGroupId, boolean empty) {
        OrderTable newOrderTable = orderTable_생성(orderTable.getId(), empty);
        newOrderTable.setTableGroupId(tableGroupId);
        return newOrderTable;
    }

    public static OrderTable orderTable_numberOfGuests_추가(OrderTable orderTable, int numberOfGuests) {
        OrderTable newOrderTable = orderTable_생성(orderTable.getId(), orderTable.isEmpty());
        newOrderTable.setNumberOfGuests(numberOfGuests);
        return newOrderTable;
    }

    public static OrderLineItem orderLineItem_생성(Long seq, Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public static OrderLineItem orderLineItem에_orderId_추가(OrderLineItem orderLineItem, Long orderId) {
        OrderLineItem newOrderLineItem = orderLineItem_생성(orderLineItem.getSeq(), orderLineItem.getMenuId(), orderLineItem.getQuantity());
        newOrderLineItem.setOrderId(orderId);
        return newOrderLineItem;
    }

    public static Order order_생성(Long id, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setId(id);
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    public static Order order_status_추가(Order order, String status) {
        Order newOrder = order_생성(order.getId(), order.getOrderLineItems());
        newOrder.setOrderStatus(status);
        return newOrder;
    }
}
