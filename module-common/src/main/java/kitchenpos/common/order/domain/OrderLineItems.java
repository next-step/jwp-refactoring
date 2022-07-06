package kitchenpos.common.order.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.common.order.dto.OrderLineItemResponse;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public OrderLineItems() {
    }

    public OrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void makeRelations(final Order order, final List<OrderLineItem> orderLineItems) {
        orderLineItems
                .stream()
                .forEach(orderLineItem -> {
                    orderLineItem.relateToOrder(order);
                    this.orderLineItems.add(orderLineItem);
                });
    }

    public List<OrderLineItemResponse> getResponses() {
        return orderLineItems
                .stream()
                .map(OrderLineItemResponse::of)
                .collect(Collectors.toList());
    }
}
