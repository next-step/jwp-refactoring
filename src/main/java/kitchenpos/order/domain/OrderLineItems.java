package kitchenpos.order.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import kitchenpos.common.exception.BadRequestException;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        validate(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void validate(List<OrderLineItem> orderLineItems) {
        if (orderLineItems == null || orderLineItems.isEmpty()) {
            throw new BadRequestException(WRONG_VALUE);
        }
    }

    public List<OrderLineItem> getValue() {
        return orderLineItems;
    }
}
