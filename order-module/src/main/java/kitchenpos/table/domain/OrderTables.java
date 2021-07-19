package kitchenpos.table.domain;

import kitchenpos.exception.OrderTableException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {

    private static final String NOT_EMPTY_TABLE_ERROR_MESSAGE = "이미 그룹핑된 테이블이 존재합니다.";
    private static final String NOT_EQUAL_TABLE_COUNT_ERROR_MESSAGE = "요청 주문 테이블 아이디 수량과 조회 결과 주문 테이블 수량이 일치하지않습니다.";

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = new ArrayList<>(orderTables);
    }

    public void checkValidEqualToRequestSize(List<Long> orderTableIds) {
        if (orderTables.isEmpty() || orderTables.size() != orderTableIds.size()) {
            throw new OrderTableException(NOT_EQUAL_TABLE_COUNT_ERROR_MESSAGE);
        }
    }

    public void checkValidEmptyTableGroup() {
        if (orderTables.stream().anyMatch(orderTable -> Objects.nonNull(orderTable.getTableGroupId()))) {
            throw new OrderTableException(NOT_EMPTY_TABLE_ERROR_MESSAGE);
        }
    }

    public void updateGrouping(TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.changeEmpty(false);
            orderTable.withTableGroup(tableGroup);
        }
    }

    public List<OrderTable> orderTables() {
        return orderTables;
    }

    public List<Long> generateOrderTableIds() {
        return orderTables.stream().map(OrderTable::getId).collect(Collectors.toList());
    }

    public void updateUnGroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    public void addOrderTable(OrderTable orderTable) {
        if (!orderTables.contains(orderTable)) {
            orderTables.add(orderTable);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        OrderTables that = (OrderTables) object;
        return Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTables);
    }

}
