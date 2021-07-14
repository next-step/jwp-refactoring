package kitchenpos.order.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class OrderStatusRequest {

  private final String orderStatus;

  @JsonCreator
  public OrderStatusRequest(@JsonProperty("orderStatus") String orderStatus) {
    this.orderStatus = orderStatus;
  }

  public String getOrderStatus() {
    return orderStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OrderStatusRequest that = (OrderStatusRequest) o;
    return Objects.equals(orderStatus, that.orderStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderStatus);
  }
}
