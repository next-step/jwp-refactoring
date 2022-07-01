package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.dto.OrderTableResponse;

@Embeddable
public class OrderTables {
    private static final int MIN_NUMBER_OF_ORDER_TABLES = 2;

    @OneToMany
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {
    }

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void makeRelations(final TableGroup tableGroup, final List<OrderTable> orderTables) {
        validateNumberOfOrderTables(orderTables);
        orderTables
                .stream()
                .forEach(orderTable -> {
                    validateOrderTableToAdd(orderTable);
                    orderTable.changeTableGroupId(tableGroup.getId());
                    this.orderTables.add(orderTable);
                });
    }

    public void validateNumberOfOrderTables(final List<OrderTable> orderTables) {
        if (orderTables.size() < MIN_NUMBER_OF_ORDER_TABLES) {
            throw new IllegalArgumentException("테이블이 2개 이상이어야 그룹을 만들 수 있습니다.");
        }
    }

    private void validateOrderTableToAdd(final OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new IllegalStateException("빈 테이블이 아니면 그룹을 만들 수 없습니다.");
        }
        if (null != orderTable.getTableGroupId()) {
            throw new IllegalStateException("이미 그룹이 지정된 테이블로 그룹을 만들 수 없습니다.");
        }
    }

    public void removeRelations() {
        orderTables.stream()
                .forEach(orderTable -> {
                    orderTable.changeTableGroupId(null);
                });
    }

    public List<OrderTableResponse> getResponses() {
        return orderTables
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }
}
