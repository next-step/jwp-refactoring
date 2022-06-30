package kitchenpos.table.domain;

import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> list;

    public OrderTables() {
        list = new ArrayList<>();
    }

    public OrderTables(List<OrderTable> orderTables) {
        list = orderTables;
    }

    public OrderTables(TableGroup tableGroup, List<OrderTable> orderTables) {
        this.list = orderTables;
        for (OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(tableGroup);
            orderTable.setEmpty(false);
        }
    }

    public void validateSizeForTableGroup(int orderTableSize) {
        if (list.size() != orderTableSize) {
            throw new IllegalArgumentException();
        }

        for (OrderTable orderTable : list) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }
    }

    public void changeEmpty(boolean empty) {
        for (OrderTable table : list) {
            table.setEmpty(empty);
        }
    }

    public void unGroupOrderTables() {
        for (OrderTable orderTable : list) {
            orderTable.setTableGroup(null);
        }
    }

    public List<OrderTable> getList() {
        return list;
    }
}
