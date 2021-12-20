package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.exception.InvalidArgumentException;

@Embeddable
public class OrderTables {
    private static final Integer MIN_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    protected void add(OrderTable orderTable) {
        if (!orderTables.contains(orderTable)) {
            orderTables.add(orderTable);
        }
    }

    protected void remove(OrderTable orderTable) {
        orderTables.remove(orderTable);
    }

    public List<OrderTable> get() {
        return orderTables;
    }

    public void clearOrderTable() {
        // TODO: 2021/12/20 주문이 진행중인(조리,식사) 테이블은 상태를 변경할 수 없다.
        for (OrderTable orderTable: orderTables) {
            orderTable.clearTableGroup();
        }
        orderTables = new ArrayList<>();
    }

    public void validateAddTables(List<OrderTable> orderTables) {
        validateTableSize(orderTables);
        validateTableEmpty(orderTables);
        validateTableGroup(orderTables);
    }

    public boolean isEmpty() {
        return orderTables.isEmpty();
    }

    private void validateTableSize(List<OrderTable> orderTables) {
        if (orderTables.size() < MIN_SIZE) {
            throw new InvalidArgumentException("두 테이블 이상이어야 단체지정이 가능합니다.");
        }
    }

    private void validateTableEmpty(List<OrderTable> orderTables) {
        if (orderTables.stream()
            .anyMatch(OrderTable::isNotEmpty)) {
            throw new InvalidArgumentException("빈 테이블만 단체지정이 가능합니다.");
        }
    }

    private void validateTableGroup(List<OrderTable> orderTables) {
        if (orderTables.stream()
            .anyMatch(OrderTable::isNotEmptyTableGroup)) {
            throw new InvalidArgumentException("다른 단체에 속한 테이블이 있습니다.");
        }
    }
}
