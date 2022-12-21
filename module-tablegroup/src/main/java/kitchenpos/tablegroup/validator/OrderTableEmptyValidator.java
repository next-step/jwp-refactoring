package kitchenpos.tablegroup.validator;

import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderTableEmptyValidator implements TableGroupValidator {

    @Override
    public void validate(OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException(
                    "비어있지 않은 주문 테이블은 단체 지정을 할 수 없습니다[orderTable" + orderTable + "]");
        }
    }
}
