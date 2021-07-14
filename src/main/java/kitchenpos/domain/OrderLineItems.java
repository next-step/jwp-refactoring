package kitchenpos.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public static OrderLineItems of(OrderLineItem... orderLineItems) {
        return new OrderLineItems(Arrays.asList(orderLineItems));
    }

    public static OrderLineItems of(List<OrderLineItem> orderLineItems) {
        return new OrderLineItems(orderLineItems);
    }

    protected OrderLineItems() {
    }

    private OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public int size() {
        return orderLineItems.size();
    }

    public boolean isEmpty() {
        return orderLineItems.isEmpty();
    }

    public boolean contains(OrderLineItem orderLineItem) {
        return orderLineItems.contains(orderLineItem);
    }

    public void forEach(Consumer<OrderLineItem> consumer) {
        orderLineItems.forEach(consumer);
    }

    public <T> List<T> mapList(Function<OrderLineItem, T> function) {
        return orderLineItems.stream()
            .map(function)
            .collect(Collectors.toList());
    }
}
