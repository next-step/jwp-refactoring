package kitchenpos.order.domain;

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
    private final List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {

    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        addOrderLineItems(orderLineItems);
    }

    public List<Long> findMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    private void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void vaildateSize(long menuCount) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목 1개 이상 있어야 합니다.");
        }

        if (orderLineItems.size() != menuCount) {
            throw new IllegalArgumentException("중복된 메뉴가 있거나 유효하지 않은 메뉴가 존재합니다.");
        }
    }
}
