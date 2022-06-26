package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable savedOrderTable = orderTableRepository.save(request.toOrderTable());
        return  OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.from(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTable) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        validateOrderTablesStatus(orderTableId);
        savedOrderTable.switchEmpty(orderTable.isEmpty());

        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    private void validateOrderTablesStatus(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTable) {
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        savedOrderTable.updateNumberOfGuests(orderTable.getNumberOfGuests());

        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }
}
