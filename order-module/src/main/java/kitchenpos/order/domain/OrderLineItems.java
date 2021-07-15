package kitchenpos.order.domain;

import kitchenpos.order.exception.NotInitMenuException;
import kitchenpos.order.exception.NotOrderLineItemsException;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        checkEmptyOrderLineItems(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void checkEmptyOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new NotOrderLineItemsException();
        }
    }

    public List<Long> toMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public List<OrderLineItem> getOrderLineItemValues() {
        return orderLineItems;
    }

    public void checkInitOrderLineItems(int countMenuSize) {
        if (orderLineItems.size() != countMenuSize) {
            throw new NotInitMenuException();
        }
    }
}
