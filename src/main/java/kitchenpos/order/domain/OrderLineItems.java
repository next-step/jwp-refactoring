package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItemEntity> orderLineItems;

    public OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItemEntity> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void updateOrder(OrderEntity orderEntity) {
        for (OrderLineItemEntity orderLineItem : orderLineItems) {
            orderLineItem.updateOrder(orderEntity);
        }

    }



    public List<OrderLineItemEntity> values() {
        return orderLineItems;
    }
}
