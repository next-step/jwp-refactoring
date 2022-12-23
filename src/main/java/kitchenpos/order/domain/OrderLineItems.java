package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    private static final String NO_ORDER_LINE_ITEM_EXCEPTION = "주문 항목이 존재하지 않습니다.";

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<OrderLineItem> orderLineItems = new ArrayList<>();

    private void validateOrderLineItemList(List<OrderLineItem> orderLineItemList) {
        if (orderLineItemList.isEmpty()) {
            throw new IllegalArgumentException(NO_ORDER_LINE_ITEM_EXCEPTION);
        }
    }

    public void addList(List<OrderLineItem> orderLineItemList) {
        validateOrderLineItemList(orderLineItemList);
        orderLineItemList.stream()
                .forEach(orderLineItems::add);
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
