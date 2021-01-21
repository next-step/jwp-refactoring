package kitchenpos.table.domain;

import kitchenpos.tableGroup.domain.TableGroup;
import org.springframework.util.CollectionUtils;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", orphanRemoval = true)
    private List<OrderTable> orderTables;

    protected OrderTables() {
        orderTables = new ArrayList<>();
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void checkOrderTables() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("그룹핑할 테이블이 부족합니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException("테이블이 사용중이거나 이미 그룹핑되어 있습니다.");
            }
        }
    }

    public static OrderTables empty() {
        return new OrderTables();
    }

    public void initialTableGroup(TableGroup tableGroup) {
        orderTables.forEach(orderTable -> orderTable.addTableGroup(tableGroup));
    }

    public void unGroupingTable() {
        orderTables.forEach(orderTable -> orderTable.removeTableGroup());
    }

    public List<Long> getOrderTablesIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public boolean hasContain(OrderTable orderTable) {
        return orderTables.contains(orderTable);
    }

    public void removeTable(OrderTable orderTable) {
        orderTables.remove(orderTable);
    }

    public void addTable(OrderTable orderTable) {
        orderTables.add(orderTable);
    }
}
