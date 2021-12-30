package kitchenpos.table.domain;

import kitchenpos.table.exception.NotCreateTableGroupException;
import kitchenpos.table.exception.TableErrorCode;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected OrderTables() {}

    protected  OrderTables(List<OrderTable> tables) {
        for (final OrderTable savedOrderTable : tables) {
            isValidOrderTable(savedOrderTable);
        }

        this.orderTables = tables;
    }

    private void isValidOrderTable(OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new NotCreateTableGroupException(TableErrorCode.ALREADY_ASSIGN_GROUP);
        }
    }

    public void assignTable(TableGroup tableGroup) {
        orderTables.forEach(savedOrderTable -> {
            savedOrderTable.assignTableGroup(tableGroup);
        });
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
