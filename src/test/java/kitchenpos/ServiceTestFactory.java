package kitchenpos;

import java.util.List;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

import java.math.BigDecimal;

public class ServiceTestFactory {
    public final static Product HONEY_COMBO = createProductBy(1L, "허니콤보", 20_000L);
    public final static Product RED_COMBO = createProductBy(2L,"레드콤보", 19_000L);
    public final static Product ORIGINAL_COMBO = createProductBy(3L,"오리지널콤보", 18_000L);
    public final static MenuGroup NEW_MENU_GROUP = createMenuGroupBy(1L, "신메뉴");
    public final static MenuGroup FAVORITE_MENU_GROUP = createMenuGroupBy(2L, "인기메뉴");
    public final static Menu HONEY_RED_COMBO = createMenuBy(1L, "허니레드콤보", 39_000L);
    public final static MenuProduct HONEY_MENU_PRODUCT = createMenuProductBy(1L, HONEY_RED_COMBO, HONEY_COMBO, 1);
    public final static MenuProduct NOT_EXISTS_MENU_PRODUCT = createMenuProductBy(2L, HONEY_RED_COMBO, ORIGINAL_COMBO, 1);
    public final static MenuProduct RED_MENU_PRODUCT = createMenuProductBy(3L, HONEY_RED_COMBO, RED_COMBO, 1);

    public static Product createProductBy(Long id, String name, long price) {
        Product product = new Product();
        //product.setId(id);
        //product.setName(name);
        //product.setPrice(BigDecimal.valueOf(price));
        return product;
    }

    public static MenuGroup createMenuGroupBy(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
//        menuGroup.setId(id);
//        menuGroup.setName(name);
        return menuGroup;
    }

    public static Menu createMenuBy(Long id, String name, long price) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        return menu;
    }

    public static MenuProduct createMenuProductBy(Long seq, Menu menu, Product product, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menu.getId());
        menuProduct.setQuantity(quantity);
        menuProduct.setProductId(product.getId());
        return menuProduct;
    }

    public static OrderTable createOrderTableBy(Long id, int numberOfGuests, boolean empty, Long tableGroupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        orderTable.setTableGroupId(tableGroupId);
        return orderTable;
    }

    public static Order createOrderBy(Long id, Long orderTableId, String orderStatus) {
        Order order = new Order();
        order.setId(id);
        order.setOrderTableId(orderTableId);
        order.setOrderStatus(orderStatus);
        return order;
    }

    public static TableGroup createTableGroupBy(Long id, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }

    public static OrderLineItem createOrderLineItemBy(Long seq, Long orderId, Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
