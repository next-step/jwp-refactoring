package kitchenpos.tobe.orders.domain.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(
        name = "order_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_order_line_item_to_orders")
    )
    private List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {
    }

    public OrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = new ArrayList<>(orderLineItems);
    }

    public List<OrderLineItem> asList() {
        return Collections.unmodifiableList(orderLineItems);
    }

    public List<Long> getMenuIds() {
        return Collections.unmodifiableList(
            orderLineItems
                .stream()
                .map(OrderLineItem::getMenuId)
                .distinct()
                .collect(Collectors.toList())
        );
    }
}
