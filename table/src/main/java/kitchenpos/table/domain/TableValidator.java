package kitchenpos.table.domain;

import org.springframework.stereotype.Component;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.exception.HasNotCompletionOrderException;
import kitchenpos.table.exception.EmptyOrderTableException;
import kitchenpos.table.exception.HasOtherTableGroupException;
import kitchenpos.table.exception.NegativeOfNumberOfGuestsException;

@Component
public class TableValidator {
    private final OrderService orderService;

    public TableValidator (
        final OrderService orderService
    ) {
        this.orderService = orderService;
    }

    public void checkOrderStatusOfOrderTable(final Long tableId) {
        Orders order = orderService.findByOrderTableId(tableId);

        if (order != null && !order.isCompletion()) {
            throw new HasNotCompletionOrderException("주문이 계산완료되지 않았습니다.");
        }
    }

    public void checkHasTableGroup(OrderTable orderTable) {
        if (orderTable.getTableGroupId() != null) {
            throw new HasOtherTableGroupException("단체지정이 된 주문테이블입니다.");
        }
    }
    
    public void checkEmptyTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new EmptyOrderTableException("주문테이블이 빈테이블입니다.");
        }
    }

    public void checkPositiveOfNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new NegativeOfNumberOfGuestsException("고객수는 음수일 수 없습니다.");
        }
    }
}
