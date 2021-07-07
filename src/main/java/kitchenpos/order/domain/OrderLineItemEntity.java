package kitchenpos.order.domain;

import javax.persistence.*;

@Table(name = "order_line_item")
@Entity
public class OrderLineItemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long seq;

  @ManyToOne
  @JoinColumn(name = "order_id", nullable = false)
  private OrderEntity order;

  @Column(name = "menu_id", nullable = false)
  private Long menuId;

  @Column(name = "quantity", nullable = false)
  private Long quantity;

  protected OrderLineItemEntity() {
  }

  public OrderLineItemEntity(Long menuId, Long quantity) {
    this.menuId = menuId;
    this.quantity = quantity;
  }

  public Long getSeq() {
    return seq;
  }

  public OrderEntity getOrder() {
    return order;
  }

  public Long getMenuId() {
    return menuId;
  }

  public Long getQuantity() {
    return quantity;
  }

  public void defineParentOrder(OrderEntity orderEntity) {
    this.order = orderEntity;
  }
}
