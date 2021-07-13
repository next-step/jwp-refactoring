package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    public static final int MINIMUM_ORDER_TABLE_SIZE = 2;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() { }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        validate();
    }

    private void validate() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException(String.format("주문테이블 리스트는 최소 %d개 이상이어야 합니다.", MINIMUM_ORDER_TABLE_SIZE));
        }
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void ungrouping() {
        orderTables.stream()
                .forEach(OrderTable::ungrouping);
    }

    public List<OrderTable> get() {
        return orderTables;
    }

    public void groupingWith(TableGroup tableGroup) {
        validateGroupingSizeWith(orderTables);
        validateOrderTablesForGrouping();
        orderTablesSetting(tableGroup);
    }

    private void validateGroupingSizeWith(List<OrderTable> savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("그룹지으려는 주문테이블 개수가 올바르지 않습니다.");
        }
    }

    private void validateOrderTablesForGrouping() {
        orderTables.stream()
                .forEach(OrderTable::validateForGrouping);
    }

    private void orderTablesSetting(TableGroup tableGroup) {
        orderTables.stream()
                .forEach(orderTable -> orderTable.groupingIn(tableGroup.getId()));
    }
}
