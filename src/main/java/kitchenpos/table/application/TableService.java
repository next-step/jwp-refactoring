package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
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
    public OrderTableResponse create(final OrderTableRequest orderTable) {
        OrderTable savedOrderTable = orderTableRepository.save(orderTable.toOrderTable());
        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTable) {
        final OrderTable savedOrderTable = findOrderTableByOrderTableId(orderTableId);
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        orders.forEach(Order::validIfNotCompletion);

        savedOrderTable.changeEmpty(orderTable.getEmpty());
        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTable) {
        final OrderTable savedOrderTable = findOrderTableByOrderTableId(orderTableId);

        savedOrderTable.changeNumberOfGuests(orderTable.getNumberOfGuests());

        return OrderTableResponse.from(savedOrderTable);
    }

    private OrderTable findOrderTableByOrderTableId(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
