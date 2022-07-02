package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private final List<OrderLineItem> elements;

    public OrderLineItems() {
        elements = new ArrayList<>();
    }

    public OrderLineItems(Order order, List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = getMenuIds(orderLineItems);
        if (orderLineItems.size() != menuIds.size()) {
            throw new IllegalArgumentException();
        }

        for (OrderLineItem item : orderLineItems) {
            item.connectedBy(order);
        }
        this.elements = orderLineItems;
    }

    private List<Long> getMenuIds(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenu)
                .map(Menu::getId)
                .distinct()
                .collect(Collectors.toList());
    }


    public List<OrderLineItem> getList() {
        return elements;
    }
}
