package kitchenpos.order.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItems {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> elements = new ArrayList<>();

    protected OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> elements) {
        validateOrderLineItems(elements);
        this.elements = new ArrayList<>(elements);
    }

    private void validateOrderLineItems(List<OrderLineItem> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            throw new IllegalArgumentException("주문 항목이 없습니다.");
        }
    }

    public List<Long> getMenuIds() {
        return elements.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public List<OrderLineItem> elements() {
        return Collections.unmodifiableList(elements);
    }

    public void validateDuplicateMenu(long menuCount) {
        if (elements.size() != menuCount) {
            throw new IllegalArgumentException("중복된 메뉴가 있습니다.");
        }
    }

    public void addOrder(Order order) {
        elements.forEach(
                orderLineItem -> orderLineItem.updateOrder(order)
        );
    }
}
