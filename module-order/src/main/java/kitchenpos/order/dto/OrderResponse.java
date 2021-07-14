package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderEntity;
import kitchenpos.order.domain.OrderLineItemEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderResponse {

  private final long id;
  private final long orderTableId;
  private final String orderStatus;
  private final LocalDateTime orderedTime;
  private final List<OrderLineItemResponse> orderLineItems;


  public OrderResponse(long id, long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItemResponse> orderLineItems) {
    this.id = id;
    this.orderTableId = orderTableId;
    this.orderStatus = orderStatus;
    this.orderedTime = orderedTime;
    this.orderLineItems = orderLineItems;
  }

  public static OrderResponse from(OrderEntity orderEntity) {
    return new OrderResponse(orderEntity.getId(),
                              orderEntity.getOrderTableId(),
                              orderEntity.getOrderStatus().name(),
                              orderEntity.getOrderedTime(),
                              toOrderLineItemResponse(orderEntity.getOrderLineItems()));
  }

  private static List<OrderLineItemResponse> toOrderLineItemResponse(List<OrderLineItemEntity> orderLineItems) {
    return orderLineItems.stream()
            .map(OrderLineItemResponse::from)
            .collect(Collectors.toList());
  }

  public static List<OrderResponse> ofList(List<OrderEntity> orders) {
    return orders.stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());
  }

  public long getId() {
    return id;
  }

  public long getOrderTableId() {
    return orderTableId;
  }

  public String getOrderStatus() {
    return orderStatus;
  }

  public LocalDateTime getOrderedTime() {
    return orderedTime;
  }

  public List<OrderLineItemResponse> getOrderLineItems() {
    return orderLineItems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OrderResponse that = (OrderResponse) o;
    return id == that.id && orderTableId == that.orderTableId && Objects.equals(orderStatus, that.orderStatus) && Objects.equals(orderedTime, that.orderedTime) && Objects.equals(orderLineItems, that.orderLineItems);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, orderTableId, orderStatus, orderedTime, orderLineItems);
  }

  public static class OrderLineItemResponse {

    private final long seq;
    private final long orderId;
    private final long menuId;
    private final long quantity;

    public OrderLineItemResponse(long seq, long orderId, long menuId, long quantity) {
      this.seq = seq;
      this.orderId = orderId;
      this.menuId = menuId;
      this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItemEntity entity) {
      return new OrderLineItemResponse(entity.getSeq(), entity.getOrder().getId(), entity.getMenuId(), entity.getQuantity());
    }

    public long getSeq() {
      return seq;
    }

    public long getOrderId() {
      return orderId;
    }

    public long getMenuId() {
      return menuId;
    }

    public long getQuantity() {
      return quantity;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      OrderLineItemResponse that = (OrderLineItemResponse) o;
      return seq == that.seq && orderId == that.orderId && menuId == that.menuId && quantity == that.quantity;
    }

    @Override
    public int hashCode() {
      return Objects.hash(seq, orderId, menuId, quantity);
    }
  }
}
