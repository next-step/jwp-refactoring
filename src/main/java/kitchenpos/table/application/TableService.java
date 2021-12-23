package kitchenpos.table.application;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
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

    public TableService(final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable persistOrderTable = orderTableRepository.save(orderTableRequest.toOrderTable());
        return OrderTableResponse.of(persistOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return OrderTableResponse.ofList(orderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);

        checkOrderStatusChangeable(orderTableId);
        savedOrderTable.changeEmpty();

        OrderTable persistTable = orderTableRepository.save(savedOrderTable);
        return OrderTableResponse.of(persistTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
        final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);

        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());

        OrderTable persistTable = orderTableRepository.save(savedOrderTable);
        return OrderTableResponse.of(persistTable);
    }

    @Transactional(readOnly = true)
    public OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional(readOnly = true)
    public void checkOrderStatusChangeable(Long orderTableId) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);

        boolean isChangeable = orders.stream()
            .allMatch(order -> order.getOrderStatus().equals(OrderStatus.COMPLETION));

        if (!isChangeable) {
            throw new IllegalArgumentException("주문 완료가 아닌 테이블은 상태를 변경할 수 없습니다.");
        }
    }
}
