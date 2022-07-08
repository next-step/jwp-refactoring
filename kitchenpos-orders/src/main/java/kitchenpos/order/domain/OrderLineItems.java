package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> values;

    protected OrderLineItems() {
        this(new ArrayList<>());
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.values = copy(orderLineItems);
    }

    private static List<OrderLineItem> copy(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream().map(OrderLineItem::from).collect(Collectors.toList());
    }

    public static OrderLineItems create() {
        return new OrderLineItems();
    }

    public List<OrderLineItem> value() {
        return Collections.unmodifiableList(values);
    }

    public void add(OrderLineItem newOrderLineItem) {
        if (hasSameMenu(newOrderLineItem)) {
            throw new IllegalArgumentException("서로 다른 주문 항목으로 같은 메뉴를 주문할 수 없습니다.");
        }
        values.add(newOrderLineItem);
    }

    private boolean hasSameMenu(OrderLineItem anotherOrderLineItem) {
        return !values.isEmpty() && values.stream()
                .anyMatch(orderLineItem -> orderLineItem.hasSameMenu(anotherOrderLineItem));
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }
}
