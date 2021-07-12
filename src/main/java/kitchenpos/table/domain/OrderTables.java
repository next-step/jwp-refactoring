package kitchenpos.table.domain;

import kitchenpos.table.exception.NotEmptyOrExistTableGroupException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
        this.orderTables = new ArrayList<>();
    }

    public OrderTables(List<OrderTable> orderTables) {
        if (!isEmptyTableAndNotExistTableGroupId(orderTables)) {
            throw new NotEmptyOrExistTableGroupException();
        }
        this.orderTables = orderTables;
    }

    private boolean isEmptyTableAndNotExistTableGroupId(List<OrderTable> orderTables) {
        return orderTables.stream()
                .allMatch(orderTable -> orderTable.isEmptyTableAndNotExistTableGroupId());
    }

    public void registerTableGroup(TableGroup tableGroup) {
        orderTables.forEach(
                orderTable -> orderTable.registerTableGroup(tableGroup)
        );
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
