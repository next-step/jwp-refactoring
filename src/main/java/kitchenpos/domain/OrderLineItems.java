package kitchenpos.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

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
        checkOrderLineItems(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void checkOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 상세 내역은 하나 이상 존재해야 합니다.");
        }

        validateOrderLineItems(orderLineItems);
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        orderLineItems.forEach(OrderLineItem::validate);
    }

    public boolean contains(OrderLineItem orderLineItem) {
        return orderLineItems.contains(orderLineItem);
    }

    public <T> List<T> mapList(Function<OrderLineItem, T> function) {
        return orderLineItems.stream()
            .map(function)
            .collect(Collectors.toList());
    }
}
