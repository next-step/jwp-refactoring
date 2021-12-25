package kitchenpos.order;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public final class OrderLineItems {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
    private final List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    protected void add(OrderLineItem orderLineItem) {
        if (!contains(orderLineItem)) {
            orderLineItems.add(orderLineItem);
        }
    }

    private boolean contains(OrderLineItem orderLineItem) {
        return orderLineItems.stream()
            .anyMatch(it -> it.equalsOrderLineItem(orderLineItem));
    }

    public List<OrderLineItem> get() {
        return orderLineItems;
    }
}
