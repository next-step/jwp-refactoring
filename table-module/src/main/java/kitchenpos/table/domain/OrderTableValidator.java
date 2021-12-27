package kitchenpos.table.domain;

import static java.util.Collections.*;

import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {
    private final OrderStatusChecker orderStatusChecker;

    public OrderTableValidator(final OrderStatusChecker orderStatusChecker) {
        this.orderStatusChecker = orderStatusChecker;
    }

    public void validateToChangeEmpty(final OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException("단체 지정이 된 주문 테이블의 상태는 변경할 수 없습니다");
        }
        if (orderStatusChecker.existsNotCompletedOrderByOrderTableIds(singletonList(orderTable.getId()))) {
            throw new IllegalArgumentException("주문 상태가 '조리' 혹은 '식사' 중일 경우, 테이블의 상태를 변경할 수 없습니다");
        }
    }
}
