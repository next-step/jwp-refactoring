package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.OrdersRepository;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.OrderTableUpdateEmptyRequest;
import kitchenpos.dto.OrderTableUpdateNumberOfGuestsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final OrdersRepository ordersRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrdersRepository ordersRepository, final OrderTableRepository orderTableRepository) {
        this.ordersRepository = ordersRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(request.getNumberOfGuests(), request.isEmpty()));
        return new OrderTableResponse(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream().map(OrderTableResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableUpdateEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findByIdAndTableGroupIsNull(orderTableId).orElseThrow(IllegalArgumentException::new);

        if (ordersRepository.existsByOrderTableInAndOrderStatusIn(Arrays.asList(savedOrderTable), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.updateEmpty(request.isEmpty());

        return new OrderTableResponse(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableUpdateNumberOfGuestsRequest request) {
        final OrderTable savedOrderTable =
                orderTableRepository.findByIdAndEmptyIsFalse(orderTableId).orElseThrow(IllegalArgumentException::new);

        savedOrderTable.updateNumberOfGuests(request.getNumberOfGuests());

        return new OrderTableResponse(savedOrderTable);
    }
}
