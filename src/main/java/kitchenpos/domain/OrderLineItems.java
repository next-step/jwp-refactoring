package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> elements = new ArrayList<>();

    public void add(OrderLineItem orderLineItem) {
        elements.add(orderLineItem);
    }

    public List<OrderLineItem> getElements() {
        return elements;
    }
}
