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
    public static OrderTable 테이블생성(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable(numberOfGuests,empty);
        return orderTable;
    }

    public static OrderTableRequest 테이블요청생성(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }


    public static Menu 메뉴생성(String name, Integer price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public static MenuProduct 메뉴상품생성(Long productId, Long quantity) {
        return new MenuProduct(productId,quantity);
    }

    public static MenuGroup 메뉴그룹생성(String name) {
        return new MenuGroup(name);
    }

    public static Product 상품생성(String name, BigDecimal price) {
        return new Product(name, Price.from(price));
    }

    public static TableGroup 테이블그룹생성(Long id, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup(orderTables);
        tableGroup.setId(id);
        return tableGroup;
    }

    public static TableGroupRequest 테이블그룹요청생성(List<Long> orderTables) {
        return new TableGroupRequest(orderTables);
    }

    public static Order 주문생성(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(orderTableId, orderLineItems);
    }

    public static OrderRequest 주문요청생성(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new OrderRequest(orderTableId, orderLineItems.stream()
                .map(OrderLineItemRequest::new)
                .collect(Collectors.toList()));
    }

    public static OrderLineItem 주문항목생성(Long menuId, long quantity) {
        return new OrderLineItem(menuId,Long.valueOf(quantity).intValue());
    }
}
