package kitchenpos.table.domain;

import kitchenpos.table.domain.exception.UngroupTableException;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup")
    @ReadOnlyProperty
    private final List<OrderTable> orderTables;

    protected OrderTables() {
        this.orderTables = new ArrayList<>();
    }

    private OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    private OrderTables(List<OrderTable> orderTables, int tableCount) {
        this(orderTables);
        validateSize(tableCount);
        validateGroupable();
    }

    private void validateSize(int tableCount) {
        if (orderTables.size() != tableCount) {
            throw new UngroupTableException("요청한 테이블 아이디 중 잘못된 아이디가 있습니다.");
        }
    }

    private void validateGroupable() {
        orderTables.forEach(OrderTable::validateTableGroupable);
    }

    public static OrderTables create(List<OrderTable> orderTables, int tableCount) {
        return new OrderTables(orderTables, tableCount);
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public List<OrderTable> getUnmodifiableList() {
        return Collections.unmodifiableList(orderTables);
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }

    public void validateNotCompletionStatus() {
        orderTables.forEach(OrderTable::validateNotCompletionStatus);
    }

    public void groupBy(OrderTableGroup orderTableGroup) {
        orderTables.forEach(orderTable -> orderTable.registerGroup(orderTableGroup));
    }
}
