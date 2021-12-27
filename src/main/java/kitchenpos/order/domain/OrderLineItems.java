package kitchenpos.order.domain;

import org.springframework.util.Assert;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {

    }

    private OrderLineItems(List<OrderLineItem> orderLineItems) {
        Assert.notEmpty(orderLineItems, "주문 시 주문 항목은 필수 입니다.");
        this.orderLineItems = orderLineItems;
    }

    public static OrderLineItems from(List<OrderLineItem> orderLineItems) {
        return new OrderLineItems(orderLineItems);
    }

    public List<OrderLineItem> getOrderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }

    public void changeOrder(Order order) {
        Assert.notNull(order, "주문은 필수입니다.");
        orderLineItems.forEach(orderLineItem ->
                orderLineItem.changeOrder(order));
    }
}
