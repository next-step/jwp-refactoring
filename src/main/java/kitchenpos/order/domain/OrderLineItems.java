package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderLineItem> list;

    public OrderLineItems() {
        list = new ArrayList<>();
    }

    public OrderLineItems(List<OrderLineItem> list) {
        this.list = list;
    }

    public List<OrderLineItem> getList() {
        return list;
    }
}
