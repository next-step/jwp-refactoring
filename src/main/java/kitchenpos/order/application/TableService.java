package kitchenpos.order.application;


import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
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

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
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
        if (orderTableRequest.isEmpty()) {
            foundOrderTable.leaveGuest();
            return OrderTableResponse.of(orderTableRepository.save(foundOrderTable));
        }
        foundOrderTable.enterGuest();
        return OrderTableResponse.of(orderTableRepository.save(foundOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        OrderTable foundOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new InputTableDataException(InputTableDataErrorCode.THE_TABLE_CAN_NOT_FIND));
        foundOrderTable.seatNumberOfGuests(numberOfGuests);
        return OrderTableResponse.of(foundOrderTable);
    }
}
