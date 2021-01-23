package kitchenpos.domain.table;

import kitchenpos.domain.BaseDateTime;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class OrderTableGroup extends BaseDateTime {
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
        checkOrderTableSize(size);
        orderTables.forEach(t -> {
            t.changeEmpty(false);
            t.changeOrderTableGroup(this);
        });
    }

    private void checkOrderTableSize(int size) {
        if (orderTables.size() != size
                || orderTables.size() < 2
                || orderTables.stream().anyMatch(t -> !t.isEmpty())) {
            throw new IllegalArgumentException("그룹화를 진행할 수 없습니다");
        }
    }

    public void applyUnGroup() {
        orderTables.forEach(t -> {
            t.checkOrderStatus();
            t.changeOrderTableGroup(null);
        });
    }
}
