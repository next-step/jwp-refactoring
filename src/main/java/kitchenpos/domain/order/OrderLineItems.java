package kitchenpos.domain.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {
    private static final String NOT_EXIST_ORDER_LINE_ITEMS = "OrderLineItems 가 존재하지 않습니다.";

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {}

    private OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public static OrderLineItems createEmpty() {
        return new OrderLineItems(new ArrayList<>());
    }

    public static OrderLineItems from(List<OrderLineItem> orderLineItems) {
        validateExistOrderLineItems(orderLineItems);
        return new OrderLineItems(orderLineItems);
    }

    public int size() {
        return orderLineItems.size();
    }

    public List<OrderLineItem> getValues() {
        return Collections.unmodifiableList(orderLineItems);
    }

    private static void validateExistOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (Objects.isNull(orderLineItems)) {
            throw new IllegalArgumentException(NOT_EXIST_ORDER_LINE_ITEMS);
        }
    }
}
