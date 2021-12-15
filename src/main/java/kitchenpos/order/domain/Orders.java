package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.Assert;

@Embeddable
class Orders {

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    protected Orders() {
    }

    static Orders empty() {
        return new Orders();
    }

    void add(Order order) {
        Assert.notNull(order, "추가하려는 주문은 필수입니다.");
        this.orders.add(order);
    }

    boolean isEmpty() {
        return orders.isEmpty();
    }

    public boolean anyCookingOrMeal() {
        return orders.stream().anyMatch(Order::isCookingOrMeal);
    }
}
