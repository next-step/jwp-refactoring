package kitchenpos.util;

import kitchenpos.domain.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestHelper {
    public static final MenuGroup 두마리_메뉴그룹 = menuGroup_생성(1L, "두마리메뉴");
    public static final MenuGroup 한마리_메뉴그룹 = menuGroup_생성(2L, "한마리메뉴");
    public static final Product 후라이드 = product_생성(1L, "후라이드", 16000);
    public static final Product 양념치킨 = product_생성(2L, "양념치킨", 16000);
    public static MenuProduct 후라이드_1개 = menuProduct_생성(1L, 후라이드.getId(), 1);
    public static MenuProduct 양념치킨_1개 = menuProduct_생성(1L, 양념치킨.getId(), 1);
    public static Menu menu = menu_생성(1L, "후라이드양념치킨", 16000, 두마리_메뉴그룹.getId(), Arrays.asList(후라이드_1개, 양념치킨_1개));
    public static TableGroup tableGroup = tableGroup_생성(1L);
    public static OrderTable orderTable = orderTable_생성(1L, tableGroup.getId(), 3);
    public static OrderLineItem orderLineItem = orderLineItem_생성(1L, menu.getId(), 2);
    public static Order order = order_생성(1L, OrderStatus.COOKING.name(), Collections.singletonList(orderLineItem));

    public static MenuGroup menuGroup_생성(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }

    public static Menu menu_생성(Long id, String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public static Product product_생성(Long id, String name, int price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }

    public static MenuProduct menuProduct_생성(Long seq, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static MenuProduct menuProduct에_menuId_추가(MenuProduct menuProduct, Long menuId) {
        menuProduct.setMenuId(menuId);
        return menuProduct;
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
        orderLineItem.setOrderId(orderId);
        return orderLineItem;
    }

    public static Order order_생성(Long id, String status, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setId(id);
        order.setOrderStatus(status);
        order.setOrderLineItems(orderLineItems);
        return order;
    }
}
