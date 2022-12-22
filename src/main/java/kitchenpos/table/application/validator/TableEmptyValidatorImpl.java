package kitchenpos.table.application.validator;

import static kitchenpos.exception.ErrorCode.CAN_NOT_ORDER;

import kitchenpos.exception.KitchenposException;
import kitchenpos.order.application.validator.TableEmptyValidator;
import kitchenpos.table.application.TableService;
import org.springframework.stereotype.Component;

@Component
public class TableEmptyValidatorImpl implements TableEmptyValidator {
    private final TableService tableService;

    public TableEmptyValidatorImpl(TableService tableService) {
        this.tableService = tableService;
    }

    @Override
    public void validateEmptyTrue(Long orderTableId) {
        if (tableService.findById(orderTableId).isEmpty()) {
            throw new KitchenposException(CAN_NOT_ORDER);
        }
    }
}
