package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItems {
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    private OrderLineItems(List<OrderLineItem> orderLineItems) {
        if(!CollectionUtils.isEmpty(orderLineItems)){
            this.orderLineItems = orderLineItems;
        }
    }

    public static OrderLineItems of(List<OrderLineItem> orderLineItems) {
        return new OrderLineItems(orderLineItems);
    }

    public void addItem(OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
    }
    public void addItems(List<OrderLineItem> orderLineItems){
        orderLineItems.forEach(this::addItem);
    }

    public void checkNotEmpty() {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenu)
                .map(Menu::getId)
                .collect(Collectors.toList());
    }

    public int count() {
        return this.orderLineItems.size();
    }

    public List<OrderLineItem> getOrderLineItems() {
        return this.orderLineItems;
    }
}
