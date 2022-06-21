package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
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
