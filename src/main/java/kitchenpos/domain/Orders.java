package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Orders {

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    public void validateOrderStatus(List<String> orderStatuses) {
        Optional<Order> findInOrderStatuses = orders.stream()
                .filter(order -> orderStatuses
                        .contains(order.getOrderStatus()))
                .findAny();

        if (findInOrderStatuses.isPresent()) {
            throw new IllegalArgumentException("조리, 식사 상태의 주문이 포함 된 주문 테이블은 상태를 변경할 수 없습니다");
        }
    }

    public void addOrder(Order order) {
        orders.add(order);
    }
}
