package kitchenpos.application.table;

import org.springframework.stereotype.Component;

import kitchenpos.application.order.OrderService;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.exception.order.HasNotCompletionOrderException;
import kitchenpos.exception.table.EmptyOrderTableException;
import kitchenpos.exception.table.HasOtherTableGroupException;
import kitchenpos.exception.table.NegativeOfNumberOfGuestsException;
import kitchenpos.exception.table.NotFoundOrderTableException;

@Component
public class TableValidator {
    private final OrderService orderService;
    private final OrderTableRepository orderTableRepository;

    public TableValidator (
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
            throw new HasNotCompletionOrderException();
        }
    }

    private void checkHasTableGroup(OrderTable orderTable) {
        if (orderTable.getTableGroupId() != null) {
            throw new HasOtherTableGroupException();
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
            throw new EmptyOrderTableException();
        }
    }

    private void checkPositiveOfNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new NegativeOfNumberOfGuestsException();
        }
    }
}
