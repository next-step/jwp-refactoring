package kitchenpos.order.domain;

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
            throw new IllegalArgumentException("주문 등록시, 주문 항목은 반드시 입력되어야 합니다");
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
            throw new IllegalArgumentException("주문 등록시, 등록 된 메뉴만 지정 가능합니다[orderLineItemsSize:" + orderLineItems.size() +
                    "/menuCount:" + menuCount + "]");
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
