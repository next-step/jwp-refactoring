package kitchenpos.table.application;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrdersRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.OrderTableUpdateEmptyRequest;
import kitchenpos.table.dto.OrderTableUpdateNumberOfGuestsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
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
        final OrderTable savedOrderTable =
                orderTableRepository.findByIdAndTableGroupIsNull(orderTableId).orElseThrow(NoSuchElementException::new);

        if (ordersRepository.existsByOrderTableInAndOrderStatusIn(Arrays.asList(savedOrderTable), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("계산 완료 상태가 아닌 테이블이 포함되어 있습니다.");
        }

        savedOrderTable.updateEmpty(request.isEmpty());

        return new OrderTableResponse(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableUpdateNumberOfGuestsRequest request) {
        final OrderTable savedOrderTable =
                orderTableRepository.findByIdAndEmptyIsFalse(orderTableId).orElseThrow(NoSuchElementException::new);

        savedOrderTable.updateNumberOfGuests(request.getNumberOfGuests());

        return new OrderTableResponse(savedOrderTable);
    }
}
