package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableService(final OrderTableRepository orderTableRepository
                            , final TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRepository.save(OrderTableRequest.toOrderTable(orderTableRequest));
        return OrderTableResponse.from(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return OrderTableResponse.ofResponses(orderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean isEmpty) {
//        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
//                                            .orElseThrow(IllegalArgumentException::new);
        final OrderTable savedOrderTable = tableValidator.findOrderTableById(orderTableId);

//        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
//                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
//            throw new OrderStatusException(OrderStatusException.ORDER_STATUS_CAN_NOT_UNGROUP_MSG);
//        }
        tableValidator.orderStatusValidate(orderTableId);

        savedOrderTable.changeEmpty(isEmpty);
        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
//        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
//                            .orElseThrow(IllegalArgumentException::new);
        final OrderTable savedOrderTable = tableValidator.findOrderTableById(orderTableId);

        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(savedOrderTable);
    }
}
