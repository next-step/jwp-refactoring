package kitchenpos.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItemBag {

    @JoinColumn(name = "order_id")
    @OneToMany(fetch = FetchType.LAZY)
    private List<OrderLineItem> orderLineItemList = new ArrayList<>();

    public OrderLineItemBag(List<OrderLineItem> orderLineItemList) {
        this.orderLineItemList = orderLineItemList;
    }

    protected OrderLineItemBag() {
    }

    public static OrderLineItemBag from(List<OrderLineItem> orderLineItemList) {
        return new OrderLineItemBag(orderLineItemList);
    }

    public List<Long> menuIds() {
        return this.orderLineItemList.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public void updateItemOrder(Order order) {
        this.checkEmptyItems();
        this.orderLineItemList.forEach(it -> it.updateOrder(order));
    }

    private void checkEmptyItems() {
        if (orderLineItemList.isEmpty()) {
            throw new IllegalArgumentException("주문 아이템이 포함되어야 합니다");
        }
    }

    public List<OrderLineItem> getOrderLineItemList() {
        return orderLineItemList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderLineItemBag that = (OrderLineItemBag) o;
        return Objects.equals(orderLineItemList, that.orderLineItemList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderLineItemList);
    }
}
