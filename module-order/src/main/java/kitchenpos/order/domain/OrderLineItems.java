package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "kitchenpos/order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<OrderLineItem> elements = new ArrayList<>();

    public OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> elements) {
        this.elements = elements;
    }

    public static List<Long> extractMenuIds(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public List<OrderLineItem> getElements() {
        return Collections.unmodifiableList(new ArrayList<>(elements));
    }

    public void add(Order order) {
        elements.stream().forEach(orderLineItem -> orderLineItem.updateOrder(order));
    }
}
