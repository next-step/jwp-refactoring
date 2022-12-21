package kitchenpos.table.validator;

import static kitchenpos.exception.ErrorCode.EXISTS_NOT_COMPLETION_STATUS;
import static kitchenpos.exception.ErrorCode.NOT_BEEN_UNGROUP;
import static kitchenpos.exception.ErrorCode.PEOPLE_LESS_THAN_ZERO;
import static kitchenpos.exception.ErrorCode.TABLE_IS_EMPTY;

import java.util.Arrays;
import java.util.Objects;
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.exception.KitchenposException;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class TableValidator {
    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))
        ) {
            throw new KitchenposException(EXISTS_NOT_COMPLETION_STATUS);
        }
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
