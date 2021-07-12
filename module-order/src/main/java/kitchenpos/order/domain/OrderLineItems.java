package kitchenpos.order.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItems {

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<OrderLineItemEntity> orderLineItemEntities = new HashSet<>();

  protected OrderLineItems() {
  }

  public void addOrderLineItem(OrderEntity orderEntity, List<OrderLineItemEntity> orderLineItems) {
    orderLineItems.forEach(orderLineItem -> orderLineItem.defineParentOrder(orderEntity));
    validateOrderLineItem(orderLineItems);
    orderLineItemEntities.addAll(orderLineItems);
  }

  public List<OrderLineItemEntity> getOrderLineItemEntities() {
    return new ArrayList<>(orderLineItemEntities);
  }

  private void validateOrderLineItem(List<OrderLineItemEntity> orderLineItems) {
    if (CollectionUtils.isEmpty(orderLineItems)) {
      throw new IllegalArgumentException();
    }
    if (isDuplicatedMenuIds(orderLineItems)) {
      throw new IllegalArgumentException();
    }
  }

  private boolean isDuplicatedMenuIds(List<OrderLineItemEntity> orderLineItems) {
    Set<Long> idSet = orderLineItems.stream()
                      .map(OrderLineItemEntity::getMenuId)
                      .collect(Collectors.toSet());
    return orderLineItems.size() != idSet.size();
  }
}
