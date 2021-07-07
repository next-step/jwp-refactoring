package kitchenpos.order.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.valueOf;

@Table(name = "orders")
@Entity
public class OrderEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "order_table_id", nullable = false)
  private Long orderTableId;

  @Enumerated(value = EnumType.STRING)
  @Column(name = "order_status", nullable = false)
  private OrderStatus orderStatus;

  @Column(name = "ordered_time", nullable = false)
  private LocalDateTime orderedTime = LocalDateTime.now();

  @Embedded
  private OrderLineItems orderLineItems = new OrderLineItems();

  protected OrderEntity() {
  }

  public OrderEntity(Long orderTableId, List<OrderLineItemEntity> items) {
    this.orderTableId = orderTableId;
    this.orderStatus = OrderStatus.COOKING;
    this.orderedTime = LocalDateTime.now();
    orderLineItems.addOrderLineItem(this, items);
  }

  public OrderEntity(Long id, Long orderTableId, List<OrderLineItemEntity> items) {
    this.id = id;
    this.orderTableId = orderTableId;
    this.orderStatus = OrderStatus.COOKING;
    this.orderedTime = LocalDateTime.now();
    this.orderLineItems.addOrderLineItem(this, items);
  }

  public OrderEntity(Long id, Long orderTableId, OrderStatus orderStatus, List<OrderLineItemEntity> items) {
    this.id = id;
    this.orderTableId = orderTableId;
    this.orderStatus = orderStatus;
    this.orderedTime = LocalDateTime.now();
    this.orderLineItems.addOrderLineItem(this, items);
  }

  public Long getId() {
    return id;
  }

  public Long getOrderTableId() {
    return orderTableId;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }

  public LocalDateTime getOrderedTime() {
    return orderedTime;
  }

  public List<OrderLineItemEntity> getOrderLineItems() {
    return orderLineItems.getOrderLineItemEntities();
  }

  public void changeStatus(String orderStatus) {
    validateBeforeChangeStatus();
    this.orderStatus = valueOf(orderStatus);
  }

  private void validateBeforeChangeStatus() {
    if (COMPLETION.equals(this.orderStatus)) {
      throw new IllegalArgumentException();
    }
  }
}
