package kitchenpos.ordertable.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import kitchenpos.common.error.ErrorEnum;
import org.springframework.util.CollectionUtils;

public class OrderTables {
    private static final int MINIMUM_ORDER_SIZE = 2;

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
            throw new IllegalArgumentException(ErrorEnum.NOT_EXISTS_ORDER_TABLE_LIST.message());
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
            throw new IllegalArgumentException(ErrorEnum.EXISTS_NOT_EMPTY_ORDER_TABLE.message());
        }
    }

    private void validateHasNotGroup() {
        boolean hasGroup = orderTables.stream()
                .anyMatch(orderTable -> orderTable.getTableGroup() != null);

        if (hasGroup) {
            throw new IllegalArgumentException(ErrorEnum.ALREADY_GROUP.message());
        }
    }
    private void validateMinimumSize(List<OrderTable> orderTables) {
        if (orderTables.size() < MINIMUM_ORDER_SIZE) {
            throw new IllegalArgumentException(ErrorEnum.ORDER_TABLE_TWO_OVER.message());
        }
    }

    public List<OrderTable> get() {
        return Collections.unmodifiableList(orderTables);
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }
}
