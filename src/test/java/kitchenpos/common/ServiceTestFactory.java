package kitchenpos.common;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceTestFactory {
    public static OrderTable 테이블생성(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroup(null);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTableRequest 테이블요청생성(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }


    public static Menu 메뉴생성(String name, Integer price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(name,price,menuGroup,menuProducts);
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

    public static TableGroupRequest 테이블그룹요청생성(List<Long> orderTables) {
        return new TableGroupRequest(orderTables);
    }

    public static Order 주문생성(Long id, Long orderTableId, String orderStatus, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderLineItems);
    }

    public static OrderRequest 주문요청생성(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new OrderRequest(orderTableId, orderLineItems.stream()
                .map(OrderLineItemRequest::new)
                .collect(Collectors.toList()));
    }

    public static OrderLineItem 주문항목생성(Long seq, Long orderId, Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
//        orderLineItem.setOrderId(orderId);
//        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
