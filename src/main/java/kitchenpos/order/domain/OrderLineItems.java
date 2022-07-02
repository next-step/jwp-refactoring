package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private final List<OrderLineItem> elements = new ArrayList<>();

    protected OrderLineItems(){

    }

    void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        elements.addAll(orderLineItems);
    }

    void includeToOrder(Order order) {
        elements.forEach(orderLineItem -> orderLineItem.includeToOrder(order));
    }

    List<Long> menuIds() {
       return elements.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public List<OrderLineItem> toList() {
        return Collections.unmodifiableList(elements);
    }
}
