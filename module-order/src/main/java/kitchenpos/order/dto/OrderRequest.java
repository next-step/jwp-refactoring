package kitchenpos.order.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.order.domain.OrderEntity;
import kitchenpos.order.domain.OrderLineItemEntity;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
  private final Long orderTableId;
  private final List<OrderLineItemRequest> orderLineItems;

  @JsonCreator
  public OrderRequest(@JsonProperty("orderTableId") Long orderTableId,
                      @JsonProperty("orderLineItems") List<OrderLineItemRequest> orderLineItems) {
    this.orderTableId = orderTableId;
    this.orderLineItems = orderLineItems;
  }

  public Long getOrderTableId() {
    return orderTableId;
  }

  public List<OrderLineItemRequest> getOrderLineItems() {
    return orderLineItems;
  }

  public List<Long> getMenuIds() {
    return orderLineItems.stream()
        .map(orderLineItemRequest -> orderLineItemRequest.menuId)
        .collect(Collectors.toList());
  }

  public OrderEntity toEntity() {
    return new OrderEntity(orderTableId, toOrderLineItemEntity());
  }

  private List<OrderLineItemEntity> toOrderLineItemEntity() {
    return orderLineItems.stream()
            .map(itemRequest -> new OrderLineItemEntity(itemRequest.menuId, itemRequest.quantity))
            .collect(Collectors.toList());
  }

  public static class OrderLineItemRequest {
    private final Long menuId;
    private final Long quantity;

    @JsonCreator
    public OrderLineItemRequest(@JsonProperty("menuId") Long menuId,
                                @JsonProperty("quantity") Long quantity) {
      this.menuId = menuId;
      this.quantity = quantity;
    }

    public Long getMenuId() {
      return menuId;
    }

    public Long getQuantity() {
      return quantity;
    }
  }
}
