package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import kitchenpos.exception.ExistGroupTableException;
import kitchenpos.exception.InvalidTableNumberException;
import kitchenpos.exception.NotEmptyException;
import kitchenpos.order.dto.OrderTableResponse;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables = orderTables;
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new InvalidTableNumberException();
        }
        for (OrderTable orderTable : orderTables) {
            validateOrderTable(orderTable);
        }
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.existTableGroup()) {
            throw new ExistGroupTableException();
        }
        if (!orderTable.isEmpty()) {
            throw new NotEmptyException();
        }
    }

    public List<OrderTable> get() {
        return orderTables;
    }

    public void ungroup() {
        this.orderTables.forEach(OrderTable::ungroupTable);
    }

    public List<OrderTableResponse> toOrderTableResponses() {
        return this.orderTables.stream()
                .map(OrderTable::toOrderTableResponse)
                .collect(Collectors.toList());
    }
}
