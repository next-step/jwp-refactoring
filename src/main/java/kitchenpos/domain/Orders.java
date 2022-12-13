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
            throw new IllegalArgumentException();
        }
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void findByInOrderStatus(List<String> orderStatuses) {
        boolean isFindByInOrderStatus = orders.stream()
                .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
        if (isFindByInOrderStatus) {
            throw new IllegalArgumentException();
        }
    }
}
