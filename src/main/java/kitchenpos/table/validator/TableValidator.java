package kitchenpos.table.validator;

import org.springframework.stereotype.Component;

@Component
public class TableValidator {

    public static final String ORDER_STATUS_NOT_COMPLETION_EXCEPTION_MESSAGE = "완료 상태만 변경 가능합니다";

    private final TableOrderValidator tableOrderValidator;

    public TableValidator(TableOrderValidator tableOrderValidator) {
        this.tableOrderValidator = tableOrderValidator;
    }

    public void validateNotComplete(Long orderTableId) {
        if (!tableOrderValidator.isComplete(orderTableId)) {
            throw new IllegalArgumentException(ORDER_STATUS_NOT_COMPLETION_EXCEPTION_MESSAGE);
        }
    }
}
