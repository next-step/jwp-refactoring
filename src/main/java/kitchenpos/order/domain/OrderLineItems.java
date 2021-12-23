package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName : kitchenpos.order.domain
 * fileName : OrderLineItems
 * author : haedoang
 * date : 2021-12-22
 * description :
 */
@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private final List<OrderLineItem> orderLineItems = new ArrayList<>();

    public List<OrderLineItem> value() {
        return orderLineItems;
    }

    public void add(Order order, List<OrderLineItem> orderLineItems) {
        orderLineItems.forEach(it -> this.orderLineItems.add(it.in(order)));
    }
}
