package kitchenpos.order.domain;

import javax.persistence.*;
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"))
    private final List<OrderLineItem> orderLineItems = new ArrayList<>();

    public void add(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
    }

    public List<OrderLineItem> value() {
        return orderLineItems;
    }
}
