package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private final List<OrderLineItem> elements;

    public OrderLineItems() {
        elements = new ArrayList<>();
    }

    public OrderLineItems(Order order, List<OrderLineItem> elements) {
        this.elements = elements;
        for (OrderLineItem item : this.elements) {
            item.connectedBy(order);
        }
    }

    public List<Long> getMenuIds() {
        return this.elements.stream()
                .map(OrderLineItem::getMenuId)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<OrderLineItem> getList() {
        return elements;
    }
}
