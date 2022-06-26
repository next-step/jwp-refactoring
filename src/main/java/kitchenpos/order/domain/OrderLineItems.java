package kitchenpos.order.domain;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    // entity 기본생성자 이므로 사용 금지
    protected OrderLineItems() {
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        validateAddOrderLineItems(orderLineItems);

        orderLineItems.forEach(this::addOrderLineItem);
    }

    private void validateAddOrderLineItems(List<OrderLineItem> orderLineItems) {
        requireNonNull(orderLineItems, "주문한 메뉴의 정보가 없습니다.");
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문한 메뉴 정보가 없습니다.");
        }
    }

    private void addOrderLineItem(OrderLineItem orderLineItem) {
        requireNonNull(orderLineItem, "주문한 메뉴 정보가 없습니다.");

        orderLineItems.add(orderLineItem);
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

}
