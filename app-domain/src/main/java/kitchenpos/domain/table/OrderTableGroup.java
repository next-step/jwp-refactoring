package kitchenpos.domain.table;

import kitchenpos.domain.BaseDateTime;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTableGroup extends BaseDateTime {
    private static final int MIN_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public OrderTableGroup() {
    }

    public Long getId() {
        return id;
    }

    public void applyGroup(int size, List<OrderTable> orderTables) {
        checkOrderTable(size, orderTables);
        orderTables.forEach(t -> {
            t.changeOrderTableStatus(false);
            t.changeOrderTableGroup(this);
        });
    }

    private void checkOrderTable(int size, List<OrderTable> orderTables) {
        if (canApplyGroup(size, orderTables)) {
            throw new IllegalArgumentException("그룹화를 진행할 수 없습니다");
        }
    }

    private boolean canApplyGroup(int size, List<OrderTable> orderTables) {
        return orderTables.size() != size
                || orderTables.size() < MIN_SIZE
                || orderTables.stream().anyMatch(t -> !t.isEmpty());
    }

    public void applyUnGroup(List<OrderTable> orderTables) {
        orderTables.forEach(t -> t.changeOrderTableGroup(null));
    }
}
