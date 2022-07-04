package kitchenpos.domain.table;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {
    private static final int MINIMUM_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private final List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {}

    public static OrderTables of(List<OrderTable> orderTables) {
        validateSize(orderTables);
        OrderTables tables = new OrderTables();
        tables.setOrderTables(orderTables);
        return tables;
    }

    private void setOrderTables(List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }

    private static void validateSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_SIZE) {
            throw new IllegalArgumentException("단체 지정은 최소 2개 테이블 이상이어야 합니다.");
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void addTableGroup(TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.registerGroupTable(tableGroup);
        }
        tableGroup.addOrderTables(this);
    }
}
