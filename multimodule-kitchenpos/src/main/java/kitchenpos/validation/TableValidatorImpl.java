package kitchenpos.validation;

import org.springframework.stereotype.Component;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.exception.HasNotCompletionOrderException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableValidator;
import kitchenpos.table.exception.EmptyOrderTableException;
import kitchenpos.table.exception.HasOtherTableGroupException;
import kitchenpos.table.exception.NegativeOfNumberOfGuestsException;
import kitchenpos.table.exception.NotFoundOrderTableException;

@Component
public class TableValidatorImpl implements TableValidator{
    private final OrderService orderService;
    private final OrderTableRepository orderTableRepository;

    public TableValidatorImpl (
        final OrderService orderService,
        final OrderTableRepository orderTableRepository
    ) {
        this.orderService = orderService;
        this.orderTableRepository = orderTableRepository;
    }
    
    public OrderTable getValidatedOrderTableForChangeEmpty(Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                                                            .orElseThrow(NotFoundOrderTableException::new);

        Orders order = orderService.findByOrderTableId(orderTable.getId());
        
        checkHasTableGroup(orderTable);
        checkOrderStatusOfOrderTable(order);

        return orderTable;
    }

    private void checkOrderStatusOfOrderTable(final Orders order) {
        if (order != null && !order.isCompletion()) {
            throw new HasNotCompletionOrderException("주문이 계산완료되지 않았습니다.");
        }
    }

    private void checkHasTableGroup(OrderTable orderTable) {
        if (orderTable.getTableGroupId() != null) {
            throw new HasOtherTableGroupException("단체지정이 된 주문테이블입니다.");
        }
    }

    public OrderTable getValidatedOrderTableForChangeNumberOfGuests(Long orderTableId, int numberOfGuests) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                                                            .orElseThrow(NotFoundOrderTableException::new);

        checkEmptyTable(orderTable);
        checkPositiveOfNumberOfGuests(numberOfGuests);

        return orderTable;
    }
    
    private void checkEmptyTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new EmptyOrderTableException("주문테이블이 빈테이블입니다.");
        }
    }

    private void checkPositiveOfNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new NegativeOfNumberOfGuestsException("고객수는 음수일 수 없습니다.");
        }
    }
}
