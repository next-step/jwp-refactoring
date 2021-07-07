package kitchenpos.order.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void addAll(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
    }

    public void validateOrderLineItemsEmpty() {
        if (CollectionUtils.isEmpty(this.orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public void validateSizeAndMenuCountDifferent(long menuCount) {
        if (isSizeDifferentFromMenuCount(menuCount)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isSizeDifferentFromMenuCount(long menuCount) {
        return orderLineItems.size() != menuCount;
    }

    public List<Long> menuIds() {
        return this.orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public List<OrderLineItem> orderLineItems() {
        return orderLineItems;
    }
}
