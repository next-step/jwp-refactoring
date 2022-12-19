package kitchenpos.order.domain;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.order.dto.MenuIdQuantityPair;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public void add(OrderLineItem orderLineItem) {
        if (!this.orderLineItems.contains(orderLineItem)) {
            this.orderLineItems.add(orderLineItem);
        }
    }

    public List<MenuIdQuantityPair> getMenuQuantityPairs() {
        return this.orderLineItems.stream()
            .map(orderLineItem -> new MenuIdQuantityPair(orderLineItem.getMenuId(), orderLineItem.getQuantity()))
            .collect(toList());
    }

}
