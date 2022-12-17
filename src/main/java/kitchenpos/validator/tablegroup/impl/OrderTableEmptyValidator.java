package kitchenpos.validator.tablegroup.impl;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.validator.tablegroup.TableGroupValidator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class OrderTableEmptyValidator extends TableGroupValidator {

    @Override
    protected void validate(OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException(
                    "비어있지 않은 주문 테이블은 단체 지정을 할 수 없습니다[orderTable" + orderTable + "]");
        }
    }
}
