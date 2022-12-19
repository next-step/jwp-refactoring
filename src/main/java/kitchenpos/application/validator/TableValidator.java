package kitchenpos.application.validator;

import static kitchenpos.exception.ErrorCode.NOT_BEEN_UNGROUP;
import static kitchenpos.exception.ErrorCode.PEOPLE_LESS_THAN_ZERO;
import static kitchenpos.exception.ErrorCode.TABLE_IS_EMPTY;

import java.util.Objects;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.KitchenposException;

public class TableValidator {
    public static void validateChangeEmpty(OrderTable orderTable) {
        isNullTableGroup(orderTable.getTableGroup());
    }

    private static void isNullTableGroup(TableGroup tableGroup) {
        if (Objects.nonNull(tableGroup)) {
            throw new KitchenposException(NOT_BEEN_UNGROUP);
        }
    }

    public static void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new KitchenposException(PEOPLE_LESS_THAN_ZERO);
        }
    }

    public static void validateEmptyTrue(OrderTable savedOrderTable){
        if (savedOrderTable.isEmpty()) {
            throw new KitchenposException(TABLE_IS_EMPTY);
        }
    }
}
