package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderMenus {
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private final List<OrderMenu> orderMenus = new ArrayList<>();

    protected OrderMenus() {
    }

    public void add(OrderMenu orderMenu) {
        orderMenus.add(orderMenu);
    }

    public List<OrderMenu> getOrderMenus() {
        return orderMenus;
    }
}
