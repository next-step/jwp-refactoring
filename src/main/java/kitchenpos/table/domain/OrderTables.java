package kitchenpos.table.domain;

import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.NotCreateTableGroupException;
import kitchenpos.table.exception.TableErrorCode;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> tables) {
//        for (final OrderTable savedOrderTable : tables) {
//            isValidOrderTable(savedOrderTable);
//        }

        this.orderTables = tables;
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        for (final OrderTable savedOrderTable : orderTables) {
            isValidOrderTable(savedOrderTable);
        }

        return new OrderTables(orderTables);
    }

    private static void isValidOrderTable(OrderTable savedOrderTable) {
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

    public List<OrderTableResponse> getOrderTableResponses() {
        return orderTables.stream()
                .map(orderTable -> OrderTableResponse.of(orderTable))
                .collect(Collectors.toList());
    }

    public boolean isCreatedOrderTable(int orderTableIdSize) {
        return orderTables.size() ==  orderTableIdSize;
    }
}
