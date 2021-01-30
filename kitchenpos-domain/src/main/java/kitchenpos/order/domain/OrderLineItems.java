package kitchenpos.order.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "orderId")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void addOrderLineItem(final OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
    }

    public void validate(long menuIdsCount) {
        this.checkEmptyOrderLineItems();
        this.checkOrderLineItemsCount(menuIdsCount);
    }

    /**
     * 주문항목의 개수가 메뉴의 수와 같은지 확인합니다.
     */
    private void checkOrderLineItemsCount(long menuIdsCount) {
        if (this.orderLineItems.size() != menuIdsCount) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 주문항목이 비었는지 확인합니다.
     */
    private void checkEmptyOrderLineItems() {
        if (CollectionUtils.isEmpty(this.orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }
}
