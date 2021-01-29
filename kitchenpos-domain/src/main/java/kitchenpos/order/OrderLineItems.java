package kitchenpos.order;

import kitchenpos.order.exception.EmptyOrderLineItemsException;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItems {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    public OrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
        validate();
    }

    private void validate() {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new EmptyOrderLineItemsException("주문 항목이 없어 등록할 수 없습니다.");
        }
    }

    public void addOrderIdToOrderLineItems(final Long orderId) {
        this.orderLineItems = orderLineItems.stream()
                .map(orderLineItem -> orderLineItem.addOrderId(orderId))
                .collect(Collectors.toList());
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
