package kitchenpos.table.domain;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import kitchenpos.common.annotation.DomainService;
import kitchenpos.common.exception.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderTableDomainValidation {

    private static final int MIN_TABLE_SIZE = 2;

    public void valid(List<OrderTable> orderTables) {
        if (isSmallThanMinTableSize(orderTables)) {
            throw new IllegalArgumentException(
                Message.ORDER_TABLES_IS_SMALL_THAN_MIN_TABLE_SIZE.getMessage());
        }
        if (isNotEmptyOrAlreadyGroup(orderTables)) {
            throw new IllegalArgumentException(
                Message.ORDER_TABLE_IS_NOT_EMPTY_TABLE_OR_ALREADY_GROUP.getMessage());
        }
    }

    private boolean isSmallThanMinTableSize(final List<OrderTable> orderTables) {
        return CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_TABLE_SIZE;
    }

    private boolean isNotEmptyOrAlreadyGroup(final List<OrderTable> orderTables) {
        return orderTables.stream().anyMatch(isTableIsNotEmptyOrGroupIsNotNull());
    }

    private Predicate<OrderTable> isTableIsNotEmptyOrGroupIsNotNull() {
        return orderTable -> OrderTableStatus.isEmpty(orderTable) ||
            Objects.nonNull(orderTable.getTableGroup());
    }
}
