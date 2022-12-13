package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItemsParam) {
        if (CollectionUtils.isEmpty(orderLineItemsParam)) {
            throw new IllegalArgumentException();
        }

        orderLineItems.addAll(orderLineItemsParam);
    }

    public List<Long> makeMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public void validateOrderLineItemsSizeAndMenuCount(long menuCount) {
        if (orderLineItems.size() != menuCount) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
