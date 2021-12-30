package kitchenpos.order.application;


import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.order.exceptions.InputTableDataErrorCode;
import kitchenpos.order.exceptions.InputTableDataException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final OrderTableValidator orderTableValidator) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        return OrderTableResponse.of(orderTableRepository.save(orderTableRequest.toEntity()));
    }

    public List<OrderTableResponse> findAll() {
        return OrderTableResponse.listOf(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable foundOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new InputTableDataException(InputTableDataErrorCode.THE_TABLE_CAN_NOT_FIND));
        final List<Order> orders = orderRepository.findAll();

        orderTableValidator.checkUpdateTableGroup(orders, foundOrderTable);
        foundOrderTable.updateEmpty(orderTableRequest.isEmpty());

        return OrderTableResponse.of(orderTableRepository.save(foundOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        OrderTable foundOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new InputTableDataException(InputTableDataErrorCode.THE_TABLE_CAN_NOT_FIND));

        orderTableValidator.checkTableEmpty(foundOrderTable);

        foundOrderTable.seatNumberOfGuests(numberOfGuests);
        return OrderTableResponse.of(foundOrderTable);
    }
}
