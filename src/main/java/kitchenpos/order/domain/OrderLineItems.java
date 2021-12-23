package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    private static final String ERROR_MESSAGE_DUPLICATE_MENU = "주문항목들 중에 중복된 메뉴가 존재합니다.";

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
            throw new IllegalArgumentException(ERROR_MESSAGE_DUPLICATE_MENU);
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
