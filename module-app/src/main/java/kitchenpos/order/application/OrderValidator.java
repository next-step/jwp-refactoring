package kitchenpos.order.application;

import java.util.Optional;

import org.springframework.stereotype.Component;

import kitchenpos.menu.domain.Menu;
import kitchenpos.common.exception.CannotCreateOrderException;
import kitchenpos.common.exception.CannotCreateOrderLineItemException;
import kitchenpos.table.domain.OrderTable;

@Component
public class OrderValidator {
    public void validateNotEmptyOrderTableExists(Optional<OrderTable> orderTableOptional) {
        orderTableOptional.ifPresent(this::validateTableIsNotEmpty);
    }

    private void validateTableIsNotEmpty(OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new CannotCreateOrderException("주문가능한 빈 테이블이 아닙니다. 테이블 ID : " + orderTable.getId());
        }
    }

    public void validateExistsMenu(Optional<Menu> menuOptional) {
        menuOptional.orElseThrow(() -> new CannotCreateOrderLineItemException("주문을 생성할 메뉴가 조회되지 않았습니다."));
    }
}
