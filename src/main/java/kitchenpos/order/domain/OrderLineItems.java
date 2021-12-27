package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import kitchenpos.order.exception.DuplicateOrderLineItemsException;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = {
        CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected void assignOrderLineItems(List<OrderLineItem> inputOrderLineItems,
        Order order) {
        validateNoDuplicateMenu(inputOrderLineItems);
        inputOrderLineItems.stream()
            .forEach(orderLineItem -> orderLineItem.assignOrder(order));
        this.orderLineItems.addAll(inputOrderLineItems);
    }

    private void validateNoDuplicateMenu(List<OrderLineItem> orderLineItems) {
        int inputSize = orderLineItems.size();
        long distinctSize = orderLineItems.stream()
            .map(orderLineItem -> orderLineItem.getMenu())
            .distinct()
            .count();

        if (distinctSize != inputSize) {
            throw new DuplicateOrderLineItemsException();
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
