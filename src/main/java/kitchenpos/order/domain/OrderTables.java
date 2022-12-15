package kitchenpos.order.domain;

import kitchenpos.common.constant.ErrorCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {}

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = new ArrayList<>(orderTables);
    }

    public void validateGroup() {
        validateShouldEmpty();
        validateHasNotGroup();
    }

    private void validateShouldEmpty() {
        boolean hasNotEmpty = orderTables.stream()
                .anyMatch(orderTable -> !orderTable.isEmpty());

        if (hasNotEmpty) {
            throw new IllegalArgumentException(ErrorCode.NOT_EMPTY_STATUS_IN_ORDER_TABLES.getMessage());
        }
    }

    private void validateHasNotGroup() {
        boolean hasGroup = orderTables.stream()
                .anyMatch(OrderTable::hasTableGroup);

        if (hasGroup) {
            throw new IllegalArgumentException(ErrorCode.ORDER_TABLES_HAS_GROUP_TABLE.getMessage());
        }
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }

    public List<OrderTable> get() {
        return Collections.unmodifiableList(orderTables);
    }
}
