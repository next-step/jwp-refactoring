package kitchenpos.ordertable.domain;

import kitchenpos.common.constant.ErrorCode;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderTables {
    private static final int MINIMUM_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {}

    public OrderTables(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = new ArrayList<>(orderTables);
    }

    private void validate(List<OrderTable> orderTables) {
        validateIsEmpty(orderTables);
        validateMinimumSize(orderTables);
    }

    private void validateIsEmpty(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new IllegalArgumentException(ErrorCode.ORDER_TABLES_IS_EMPTY.getMessage());
        }
    }

    private void validateMinimumSize(List<OrderTable> orderTables) {
        if (orderTables.size() < MINIMUM_SIZE) {
            throw new IllegalArgumentException(ErrorCode.ORDER_TABLES_MINIMUM_IS_TWO.getMessage());
        }
    }

    public void validateGroup() {
        validateShouldEmpty();
        validateHasNotGroup();
    }

    private void validateShouldEmpty() {
        boolean hasNotEmpty = orderTables.stream()
                .anyMatch(orderTable -> !orderTable.isEmpty());

        if (hasNotEmpty) {
            throw new IllegalArgumentException(ErrorCode.ORDER_TABLES_IS_NOT_EMPTY.getMessage());
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
