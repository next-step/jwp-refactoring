package kitchenpos.domain.order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
    @JoinColumn(name = "orderId")
    private List<OrderLineItem> list = new ArrayList<>();

    public OrderLineItems() {
    }

    public void add(OrderLineItem orderLineItem) {
        this.list.add(orderLineItem);
    }

    public List<OrderLineItem> getAll() {
        return this.list;
    }
}
