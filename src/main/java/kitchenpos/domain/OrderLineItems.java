package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.exception.KitchenposErrorCode;
import kitchenpos.exception.KitchenposException;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        this.orderLineItems = orderLineItemRequests.stream()
            .map(OrderLineItemRequest::toEntity)
            .collect(Collectors.toList());
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

    public List<Long> getIds() {
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
