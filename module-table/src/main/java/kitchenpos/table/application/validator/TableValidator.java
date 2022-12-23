package kitchenpos.table.application.validator;


import static kitchenpos.common.exception.ErrorCode.NOT_BEEN_UNGROUP;
import static kitchenpos.common.exception.ErrorCode.PEOPLE_LESS_THAN_ZERO;
import static kitchenpos.common.exception.ErrorCode.TABLE_IS_EMPTY;

import java.util.Objects;
import kitchenpos.common.exception.KitchenposException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.springframework.stereotype.Component;

@Component
public class TableValidator {
    private final OrderStatusValidator orderStatusValidator;

    public TableValidator(OrderStatusValidator orderStatusValidator) {
        this.orderStatusValidator = orderStatusValidator;
    }

    public void validateChangeEmpty(OrderTable orderTable) {
        isNullTableGroup(orderTable.getTableGroup());
        existsCookingOrMeal(orderTable.getId());
    }

    private void isNullTableGroup(TableGroup tableGroup) {
        if (Objects.nonNull(tableGroup)) {
            throw new KitchenposException(NOT_BEEN_UNGROUP);
        }
    }

    private void existsCookingOrMeal(Long orderTableId) {
        orderStatusValidator.existsByOrderTableIdAndOrderStatusIn(orderTableId);
    }

    public void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new KitchenposException(PEOPLE_LESS_THAN_ZERO);
        }
    }

    public void validateEmptyTrue(OrderTable savedOrderTable){
        if (savedOrderTable.isEmpty()) {
            throw new KitchenposException(TABLE_IS_EMPTY);
        }
    }
}
