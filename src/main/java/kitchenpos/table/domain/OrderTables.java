package kitchenpos.table.domain;

import kitchenpos.table.dto.OrderTableResponse;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> tables) {
        this.orderTables = tables;
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
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    public boolean isCreatedOrderTable(int orderTableIdSize) {
        return orderTables.size() == orderTableIdSize;
    }

    public void validateOrderTable() {
        orderTables.forEach(OrderTable::availableCreate);
    }

    public void unGroup() {
        orderTables.forEach(OrderTable::unGroup);
    }
}
