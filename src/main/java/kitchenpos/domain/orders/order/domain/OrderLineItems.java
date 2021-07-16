package kitchenpos.domain.orders.order.domain;

import org.springframework.util.CollectionUtils;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
class OrderLineItems {
    @Transient
    private Order order;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    protected OrderLineItems(final Order order) {
        this.order = order;
    }

    protected void add(final OrderLineItem orderLineItem) {
        if (orderLineItem == null) {
            throw new IllegalArgumentException("주문항목이 존재하지 않습니다.");
        }

        orderLineItem.order(order);
        this.orderLineItems.add(orderLineItem);
    }

    protected void add(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문항목이 존재하지 않습니다.");
        }

        orderLineItems.stream().forEach(this::add);
    }

    protected List<OrderLineItem> list() {
        return Collections.unmodifiableList(orderLineItems);
    }

    protected boolean isSameSize(Long size) {
        return orderLineItems.size() == size;
    }
}