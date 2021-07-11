package kitchenpos.table.domain;

import kitchenpos.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableEntity;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.tablegroup.domain.TableGroupEntity;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTableEntity> orderTables;

    public static final int MINIMUM_SIZE = 2;

    protected OrderTables() {
    }

    public OrderTables(List<OrderTableEntity> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableEntity> values() {
        return Collections.unmodifiableList(orderTables);
    }

    public void updateTableGroup(TableGroupEntity tableGroupEntity) {
        for (OrderTableEntity orderTable : orderTables) {
            updatePossible(orderTable);
            orderTable.updateTableGroup(tableGroupEntity);
        }
    }

    private void updatePossible(OrderTableEntity orderTable) {
        orderTable.isEmptyCheck();
        orderTable.hasTableGroupIdCheck();
    }

    public void releaseGroup() {
        for (OrderTableEntity orderTable : orderTables) {
            orderTable.releaseGroup();
        }
    }

}
