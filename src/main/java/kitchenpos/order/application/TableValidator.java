package kitchenpos.order.application;

import kitchenpos.order.application.exception.InvalidOrderState;
import kitchenpos.order.application.exception.InvalidTableState;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableState;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TableValidator {

    public void validate(OrderTable orderTable) {
        validateTableState(orderTable);
        validateOrderStatus(orderTable);
        validateTableGroup(orderTable);
    }

    public void validateTableState(OrderTable orderTable) {
        TableState tableState = orderTable.getTableState();

        if (tableState.isEmpty()) {
            throw new InvalidTableState("빈 테이블 입니다.");
        }
    }

    private void validateOrderStatus(OrderTable orderTable) {
        OrderStatus orderStatus = orderTable.getOrderStatus();

        if (!orderStatus.isCompleted()) {
            throw new InvalidOrderState("주문 상태가 완료되지 않아 테이블을 정리할 수 없습니다.");
        }
    }

    private void validateTableGroup(OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new InvalidTableState("테이블에 일행이 있습니다.");
        }
    }
}
