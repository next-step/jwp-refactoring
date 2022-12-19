package kitchenpos.order.domain;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import kitchenpos.common.exception.InvalidParameterException;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderLineItems {
    private static final String ERROR_MESSAGE_ORDER_LINE_ITEM_IS_EMPTY = "주문 항목은 비어있을 수 없습니다.";

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderLineItem> items = new ArrayList<>();

    protected OrderLineItems() {}

    private OrderLineItems(List<OrderLineItem> items) {
        validate(items);
        this.items = new ArrayList<>(items);
    }

    private void validate(List<OrderLineItem> items) {
        validateEmpty(items);
    }

    private void validateEmpty(List<OrderLineItem> items) {
        if (CollectionUtils.isEmpty(items)) {
            throw new InvalidParameterException(ERROR_MESSAGE_ORDER_LINE_ITEM_IS_EMPTY);
        }
    }

    public static OrderLineItems from(List<OrderLineItem> items) {
        return new OrderLineItems(items);
    }

    public static OrderLineItems of(OrderLineItem... items) {
        return new OrderLineItems(Arrays.asList(items));
    }

    public List<OrderLineItem> list() {
        return Collections.unmodifiableList(items);
    }
}
