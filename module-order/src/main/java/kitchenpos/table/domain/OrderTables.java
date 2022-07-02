package kitchenpos.table.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {
    private static final int MINIMUM_GROUP_TABLE_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup")
    private final List<OrderTable> elements = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(final TableGroup tableGroup, final List<OrderTable> orderTables) {
        addAll(tableGroup, orderTables);
    }

    public List<OrderTable> getElements() {
        return elements;
    }

    public void addAll(final TableGroup tableGroup, final List<OrderTable> orderTables) {
        validate(orderTables);
        for (OrderTable orderTable : orderTables) {
            elements.add(orderTable);
            orderTable.setTableGroup(tableGroup);
        }
    }

    private void validate(final List<OrderTable> orderTables) {
        if (orderTables.size() < MINIMUM_GROUP_TABLE_SIZE) {
            throw new IllegalArgumentException("테이블 수가 2개 이상이어야 단체 지정할 수 있습니다");
        }

        if (orderTables.stream().anyMatch(table -> !table.isEmpty())) {
            throw new IllegalArgumentException("빈 테이블들만 단체 지정할 수 있습니다.");
        }

        if (orderTables.stream().anyMatch(OrderTable::hasGroup)) {
            throw new IllegalArgumentException("이미 단체 지정된 테이블이 있어서 단체 지정할 수 없습니다.");
        }
    }

    public void ungroup() {
        elements.forEach(table -> table.setTableGroup(null));
    }
}
