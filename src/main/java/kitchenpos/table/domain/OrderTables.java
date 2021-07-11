package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    private static final String NOT_VALID_FOR_TABLE_GROUP = "테이블 그룹을 지정할 수 없는 상태입니다.";

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public boolean checkTableSizeNotEnough() {
        return CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2;
    }

    public int size() {
        return orderTables.size();
    }

    public void assignTableGroup(TableGroup tableGroup) {
        checkOrderTableValid();
        orderTables.forEach(orderTable -> orderTable.assignTableGroup(tableGroup.getId()));
    }

    public void checkOrderTableValid() {
        orderTables.stream()
                .filter(OrderTable::checkIsNotValidTableGroup)
                .findAny()
                .ifPresent(v -> {
                    throw new IllegalStateException(NOT_VALID_FOR_TABLE_GROUP);
                });
    }

    public void upgroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }
}

