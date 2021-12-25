package kitchenpos.table;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.common.exception.InvalidArgumentException;

@Embeddable
public class OrderTables {

    private static final Integer MIN_SIZE = 2;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    protected void add(OrderTable orderTable) {
        if (!orderTables.contains(orderTable)) {
            orderTables.add(orderTable);
        }
    }

    public List<OrderTable> get() {
        return orderTables;
    }

    public void clearOrderTable() {
        for (OrderTable orderTable : orderTables) {
            orderTable.validateOnGoingOrder();
        }
        orderTables = new ArrayList<>();
    }

    public boolean isEmpty() {
        return orderTables.isEmpty();
    }

    public void addOrderTables(List<OrderTable> orderTables) {
        validateAddTables(orderTables);

        for (OrderTable orderTable : orderTables) {
            this.orderTables.add(orderTable.changeEmpty(false));
        }
    }

    protected void validateAddTables(List<OrderTable> orderTables) {
        validateTableSize(orderTables);
        validateTableEmpty(orderTables);
        validateTableGroup(orderTables);
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
