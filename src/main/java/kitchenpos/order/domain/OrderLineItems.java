package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

import kitchenpos.common.exception.KitchenposErrorCode;
import kitchenpos.common.exception.KitchenposException;

@Embeddable
public class OrderLineItems {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> orderLineItemRequests) {
        this.orderLineItems = new ArrayList<>(orderLineItemRequests);
        checkNotEmpty();
    }

    private void checkNotEmpty() {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new KitchenposException(KitchenposErrorCode.EMPTY_ORDER_LINE_ITEMS);
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void add(OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());
    }

    public void validateSize(long count) {
        if (orderLineItems.size() != count) {
            throw new KitchenposException(KitchenposErrorCode.INVALID_ORDER_LINE_ITEM_SIZE);
        }
    }
}
