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

    protected void assignOrderLineItems(List<OrderLineItem> orderLineItems) {
        validateNoDuplicateMenu(orderLineItems);
        this.orderLineItems.addAll(orderLineItems);
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
