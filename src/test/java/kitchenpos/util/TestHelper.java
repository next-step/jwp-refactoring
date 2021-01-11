package kitchenpos.util;

import kitchenpos.domain.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestHelper {
    public static final MenuGroup 두마리_메뉴그룹 = menuGroup_생성(1L, "두마리메뉴");
    public static final Product 후라이드 = product_생성(1L, "후라이드", BigDecimal.valueOf(16000));
    public static final Product 양념치킨 = product_생성(2L, "양념치킨", BigDecimal.valueOf(16000));
    public static final MenuProduct 후라이드_1개 = menuProduct_생성(1L, 후라이드.getId(), 1);
    public static final MenuProduct 양념치킨_1개 = menuProduct_생성(1L, 양념치킨.getId(), 1);
    public static final Menu menu = menu_생성(1L, "후라이드양념치킨", BigDecimal.valueOf(16000), 두마리_메뉴그룹.getId(),
            Arrays.asList(후라이드_1개, 양념치킨_1개));

    public static final TableGroup tableGroup = tableGroup_생성(1L);
    public static final OrderTable orderTable = orderTable_생성(1L, tableGroup.getId(), 3);
    public static final OrderLineItem orderLineItem = orderLineItem_생성(1L, menu.getId(), 2);
    public static final Order order = order_생성(1L, Collections.singletonList(orderLineItem));
    public static final Order 주문항목이_없는_order = order_생성(1L, null);
    public static final Order 요리중_order = order_status_추가(order, OrderStatus.COOKING.name());
    public static final Order 식사_order = order_status_추가(order, OrderStatus.MEAL.name());
    public static final Order 완료된_order = order_status_추가(order, OrderStatus.COMPLETION.name());

    public static MenuGroup menuGroup_생성(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }

    public static Menu menu_생성(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static Menu menu_price_변경(Menu menu, BigDecimal price) {
        return menu_생성(menu.getId(), menu.getName(), price, menu.getMenuGroupId(), menu.getMenuProducts());
    }

    public static Product product_생성(Long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static Product product_price_변경(Product product, BigDecimal price) {
        return product_생성(product.getId(), product.getName(), price);
    }

    public static MenuProduct menuProduct_생성(Long seq, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static MenuProduct menuProduct에_menuId_추가(MenuProduct menuProduct, Long menuId) {
        MenuProduct newMenuProduct = menuProduct_생성(menuProduct.getSeq(), menuProduct.getProductId(), menuProduct.getQuantity());
        newMenuProduct.setMenuId(menuId);
        return newMenuProduct;
    }

    public static TableGroup tableGroup_생성(Long id) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        return tableGroup;
    }

    public static OrderTable orderTable_생성(Long id, Long tableGroupId, int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
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
