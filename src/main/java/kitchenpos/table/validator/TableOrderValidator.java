package kitchenpos.table.validator;

import org.springframework.stereotype.Component;

@Component
public interface TableOrderValidator {

    boolean isComplete(Long orderTableId);
}
