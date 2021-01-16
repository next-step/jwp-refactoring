package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Orders {
    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    protected Orders() {
    }

    public void checkOrderStatus() {
        if (orders.stream().anyMatch(order -> OrderStatus.isUnchangeableStatus(order.getOrderStatus()))) {
            throw new IllegalArgumentException("주문 상태가 조리중이거나 식사중인 테이블의 공석 여부는 변경할 수 없습니다.");
        }
    }
}
