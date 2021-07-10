package kitchenpos.order.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItems {

    public static final String ORDER_LINE_ITEM_IS_EMPTY = "주문 항목이 존재하지 않습니다.";
    public static final String NOT_MATCH_MENU_SIZE = "조회 된 메뉴의 수와 다릅니다.";

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        validateIsEmpty(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    public void mappingOrder(Order order) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.mappingOrder(order));
    }

    private void validateIsEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException(ORDER_LINE_ITEM_IS_EMPTY);
        }
    }

    public List<Long> menuIds() {
        return orderLineItems.stream()
                .map(OrderLineItem::menuId)
                .collect(Collectors.toList());
    }

    public void validateMenuDataSize(int menuDataSize) {
        if (orderLineItems.size() != menuDataSize) {
            throw new IllegalArgumentException(NOT_MATCH_MENU_SIZE);
        }
    }

    public List<OrderLineItem> orderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }
}
