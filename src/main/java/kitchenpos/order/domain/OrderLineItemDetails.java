package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import kitchenpos.menu.domain.MenuDetailOption;

@Embeddable
public class OrderLineItemDetails {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_line_item_seq", foreignKey = @ForeignKey(name = "fk_order_menu_product_order_line_item"))
    private List<OrderLineItemDetail> orderLineItemDetails = new ArrayList<>();

    public static OrderLineItemDetails of(OrderLineItemDetail... orderLineItemDetails) {
        return new OrderLineItemDetails(Arrays.asList(orderLineItemDetails));
    }

    public static OrderLineItemDetails of(List<OrderLineItemDetail> orderLineItemDetails) {
        return new OrderLineItemDetails(orderLineItemDetails);
    }

    protected OrderLineItemDetails() {
    }

    public OrderLineItemDetails(List<OrderLineItemDetail> orderLineItemDetails) {
        this.orderLineItemDetails = orderLineItemDetails;
    }

    public List<MenuDetailOption> toMenuDetailOptions() {
        return orderLineItemDetails.stream()
            .map(OrderLineItemDetail::toMenuDetailOption)
            .collect(Collectors.toList());
    }
}
