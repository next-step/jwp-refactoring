package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public void add(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
    }

    public void setOrder(final Order order) {
        this.orderLineItems.forEach(orderLineItem -> orderLineItem.setOrder(order));
    }


    public <R> List<R> convertAll(Function<OrderLineItem, R> converter) {
        return this.orderLineItems.stream()
                                  .map(converter)
                                  .collect(Collectors.toList());
    }
}
