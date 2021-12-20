package kitchenpos.order;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class OrderFactory {
  public static OrderLineItem ofOrderLineItem(Long seq, Long orderId, Long menuId, Long quantity) {
    OrderLineItem orderLineItem = new OrderLineItem();
    orderLineItem.setSeq(seq);
    orderLineItem.setOrderId(orderId);
    orderLineItem.setMenuId(menuId);
    orderLineItem.setQuantity(quantity);

    return orderLineItem;
  }

  public static OrderLineItem ofOrderLineItem(Long menuId, Long quantity) {
    return ofOrderLineItem(null, null, menuId, quantity);
  }

  public static Order ofOrder(Long id, Long orderTableId, String orderStatus, List<OrderLineItem> orderLineItems, LocalDateTime orderedTime) {
    Order order = new Order();
    order.setId(id);
    order.setOrderTableId(orderTableId);
    order.setOrderStatus(orderStatus);
    order.setOrderLineItems(orderLineItems);
    order.setOrderedTime(orderedTime);

    return order;
  }

  public static Order ofOrder(long orderTableId) {
    return ofOrder(null, orderTableId, null, null, null);
  }

  public static OrderTable ofOrderTable(Long id, Long tableGroupId, boolean empty, int numberOfGuests) {
    OrderTable orderTable = new OrderTable();
    orderTable.setId(id);
    orderTable.setTableGroupId(tableGroupId);
    orderTable.setEmpty(empty);
    orderTable.setNumberOfGuests(numberOfGuests);

    return orderTable;
  }

  public static OrderTable ofOrderTable(boolean empty, int numberOfGuests) {
    return ofOrderTable(null, null, empty, numberOfGuests);
  }

  public static TableGroup ofTableGroup(Long id, List<OrderTable> orderTables, LocalDateTime createdDate) {
    TableGroup tableGroup = new TableGroup();
    tableGroup.setId(id);
    tableGroup.setOrderTables(orderTables);
    tableGroup.setCreatedDate(createdDate);

    return tableGroup;
  }

  public static TableGroup ofTableGroup(List<OrderTable> orderTables) {
    return ofTableGroup(null, orderTables, null);
  }
}
