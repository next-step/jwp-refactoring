package kitchenpos.ordertable.validator;

import java.util.Objects;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class AlreadyGroupedOrderTableValidator implements OrderTableValidator {

    @Override
    public void validate(OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException("이미 단체 지정이 된 주문 테이블입니다[" + orderTable + "]");
        }
    }
}
