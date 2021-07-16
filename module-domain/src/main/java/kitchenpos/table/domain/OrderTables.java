package kitchenpos.table.domain;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderTables {
    @OneToMany
    @JoinColumn(name = "tableGroupId")
    private List<OrderTable> orderTables;

    public static final int MINIMUM_SIZE = 2;

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> values() {
        return Collections.unmodifiableList(orderTables);
    }

    public void updateTableGroup(Long tableGroupId) {
        for (OrderTable orderTable : orderTables) {
            orderTable.updateTableGroup(tableGroupId);
        }
    }

    public void releaseGroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.releaseGroup();
        }
    }

}
