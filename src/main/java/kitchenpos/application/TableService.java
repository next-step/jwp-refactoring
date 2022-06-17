package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.EmptyRequest;
import kitchenpos.dto.NumberOfGuestsRequest;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository,
                        OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(OrderTableRequest request) {
        return OrderTableResponse.of(orderTableRepository.save(request.toOrderTable()));
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.of(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, EmptyRequest request) {
        OrderTableEntity orderTable = orderTableRepository.findById(orderTableId)
                                                          .orElseThrow(NotFoundOrderTableException::new);
        if (hasUncompletedOrder(orderTableId)) {
            throw new CannotChangeEmptyException();
        }
        orderTable.changeEmpty(request.isEmpty());
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    private boolean hasUncompletedOrder(Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, NumberOfGuestsRequest request) {
        OrderTableEntity orderTable = orderTableRepository.findById(orderTableId)
                                                          .orElseThrow(NotFoundOrderTableException::new);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }
}
