package kitchenpos.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.exception.TableGroupException;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTablesManager {

    @OneToMany(mappedBy = "tableGroup", cascade = {CascadeType.MERGE})
    private List<OrderTable> orderTables = new LinkedList<>();

    protected OrderTablesManager() {

    }

    public void mapOrderTables(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            this.orderTables.add(orderTable);
        }
        validateOrderTableCanGroup();
    }

    private void validateOrderTableCanGroup() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new TableGroupException("단체테이블은 2개 이상이여야 합니다");
        }
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new TableGroupException("단체테이블은 2개 이상이여야 합니다");
            }
        }
    }

}
