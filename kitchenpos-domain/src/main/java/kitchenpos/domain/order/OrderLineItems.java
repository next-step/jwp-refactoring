package kitchenpos.domain.order;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "orderId")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public void add(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
    }

    public <R> List<R> convertAll(Function<OrderLineItem, R> converter) {
        return this.orderLineItems.stream()
                                  .map(converter)
                                  .collect(Collectors.toList());
    }
}
