package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;

@Transactional
@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = new OrderTable(orderTableRequest.getNumberOfGuests());
        final OrderTable saved = orderTableRepository.save(orderTable);

        return OrderTableResponse.of(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
            .stream()
            .map(OrderTableResponse::of)
            .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        validateOrdersStatus(new Orders(findAllOrder(orderTableId)));
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());
        final OrderTable saved = orderTableRepository.save(savedOrderTable);

        return OrderTableResponse.of(saved);
    }

    private void validateOrdersStatus(final Orders orders) {
        if (orders.isNotCompleted()) {
            throw new IllegalArgumentException("주문이 완료되지 않은 테이블은 변경할 수 없습니다.");
        }
    }

    private List<Order> findAllOrder(final Long orderTableId) {
        return orderRepository.findAllByOrderTable_Id(orderTableId);
    }

    private OrderTable findOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .filter(orderTable -> orderTable.getTableGroupId() == null)
            .orElseThrow(IllegalArgumentException::new);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        validateNumberOfGuests(orderTableRequest);
        final OrderTable savedOrderTable = findOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        final OrderTable saved = orderTableRepository.save(savedOrderTable);

        return OrderTableResponse.of(saved);
    }

    private void validateNumberOfGuests(final OrderTableRequest orderTableRequest) {
        if (orderTableRequest.isNotValidNumberOfGuests()) {
            throw new IllegalArgumentException("손님 숫자는 0이상 입니다.");
        }
    }
}
