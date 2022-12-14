package kitchenpos.table.application;


import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.NumberOfGuestsRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
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
    public OrderTableResponse changeEmpty(Long orderTableId, boolean isEmpty) {
        OrderTable orderTable = findById(orderTableId);
        if (hasUncompletedOrder(orderTableId)) {
            throw new IllegalArgumentException();
        }
        orderTable.changeEmpty(isEmpty);
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    private boolean hasUncompletedOrder(Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, OrderStatus.getUncompletedStatuses());
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, NumberOfGuestsRequest request) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                                                    .orElseThrow(IllegalArgumentException::new);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    public OrderTable findById(Long id) {
        return orderTableRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
