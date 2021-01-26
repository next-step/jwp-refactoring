package kitchenpos.domain.table;

import kitchenpos.domain.BaseDateTime;
import kitchenpos.domain.order.Orders;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class OrderTableGroup extends BaseDateTime {
    private static final int MIN_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "orderTableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTableGroup() {}

    public OrderTableGroup(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void applyGroup(int size) {
        checkOrderTable(size);
        orderTables.forEach(t -> {
            t.changeOrderTableStatus(false);
            t.changeOrderTableGroup(this);
        });
    }

    private void checkOrderTable(int size) {
        if (canApplyGroup(size)) {
            throw new IllegalArgumentException("그룹화를 진행할 수 없습니다");
        }
    }

    private boolean canApplyGroup(int size) {
        return orderTables.size() != size
                || orderTables.size() < MIN_SIZE
                || orderTables.stream().anyMatch(t -> !t.isEmpty());
    }

    public void applyUnGroup() {
        orderTables.forEach(t -> t.changeOrderTableGroup(null));
    }

    public List<Orders> getOrdersOfTables() {
        return orderTables.stream().flatMap(t -> t.getOrders().stream()).collect(toList());
    }
}
