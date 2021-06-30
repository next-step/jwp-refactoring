package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "seq", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderLineItem> data;

    protected OrderLineItems() {
        data = new ArrayList<>();
    }

    public OrderLineItems(List<OrderLineItem> data) {
        this.data = data;
    }

    public void add(OrderLineItem item) {
        data.add(item);
    }

    public List<OrderLineItem> getData() {
        return data;
    }
}
