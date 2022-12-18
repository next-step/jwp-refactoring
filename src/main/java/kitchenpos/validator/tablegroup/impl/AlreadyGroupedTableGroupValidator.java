package kitchenpos.validator.tablegroup.impl;

import java.util.Objects;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.validator.tablegroup.TableGroupValidator;
import org.springframework.stereotype.Component;

@Component
public class AlreadyGroupedTableGroupValidator implements TableGroupValidator {

    @Override
    public void validate(OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException("이미 단체 지정이 된 주문 테이블이 포함되어 있습니다[orderTable:" + orderTable + "]");
        }
    }
}
